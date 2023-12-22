import { Request, Response, NextFunction } from "express";
import BaseController from "./BaseController";
import logger from "../utils/logger";
import * as tf from "@tensorflow/tfjs-node";
import * as path from "path";
import cpyBucketFiles from "../utils/bucketObjectHandler";
import { CONST } from "../utils/constant";

const BASE_PATH =
  CONST.ENVIRONMENT === "dev" ? path.join(__dirname, "../../") : "/tmp/";

class BloodAnalysisController extends BaseController {
  model: tf.LayersModel | undefined;

  constructor() {
    super();
    cpyBucketFiles(
      "feminacare-ml-models",
      "klasifikasiWarna/",
      "./models/klasifikasiWarna"
    ).then(() => {
      this.loadModel();
    });
  }

  async loadModel() {
    try {
      const modelPath = path.join(
        BASE_PATH,
        "models/klasifikasiWarna/model.json"
      );
      this.model = await tf.loadLayersModel(`file://${modelPath}`);
      logger.info("Blood analysis model loaded");
    } catch (err: unknown) {
      logger.error(err);
    }
  }

  getClassLabel(classIndex: number): string {
    switch (classIndex) {
      case 0:
        return "Orange";
      case 1:
        return "Pink";
      case 2:
        return "Abu-Abu";
      case 3:
        return "Coklat";
      case 4:
        return "Hitam";
      case 5:
        return "Merah";
      default:
        return "Unknown";
    }
  }

  getResult = async (req: Request, res: Response, next: NextFunction) => {
    try {
      if (!req.file) {
        res.status(400).send({
          message: "image is required",
        });
      }
      const image = req.file?.buffer;
      const uint8array = new Uint8Array(image as Buffer);
      let imageTensor = tf.node.decodeImage(uint8array, 3); // 3 for RGB images

      // Resize the image to the target size
      imageTensor = tf.image.resizeBilinear(imageTensor, [300, 300]);

      // Normalize the pixel values
      imageTensor = imageTensor.div(tf.scalar(255));

      // Add a dimension
      imageTensor = imageTensor.expandDims(0);

      // predict
      const prediction = this.model?.predict(imageTensor) as tf.Tensor;
      const classIndex = prediction
        ? tf.argMax(prediction, 1).dataSync()[0]
        : -1;
      const bloodColor = this.getClassLabel(classIndex);

      res.json({
        data: {
          colorIndex: bloodColor,
        },
        message: "Blood Analysis Finished Sucessfully",
      });
      logger.info(`Blood analysis return ${bloodColor}`);
    } catch (err: unknown) {
      logger.error(err);
      res.status(500).send({
        message: "internal server error",
      });
    }
  };
}
export default new BloodAnalysisController();
