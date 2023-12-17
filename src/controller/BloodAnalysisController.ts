import { Request, Response, NextFunction } from "express";
import BaseController from "./BaseController";
import logger from "../utils/logger";
import * as tf from "@tensorflow/tfjs-node";
import * as fs from "fs";
import * as path from "path";
import storageClient from "../utils/cloudStorageClient";

class BloodAnalysisController extends BaseController {
  model: tf.LayersModel | undefined;

  constructor() {
    super();
    this.readBinaryFiles();
    // this.loadModel(
    //   "C:/Users/ACER/Documents/GitHub/Feminicare/model/model.json"
    // );
  }

  async readBinaryFiles() {
    try {
      const bucketName = "feminacare-ml-models";
      const [files] = await storageClient
        .bucket(bucketName)
        .getFiles({ prefix: "model/" });

      const dir = "./models/klasifikasiWarna";

      if (!fs.existsSync(dir)) {
        fs.mkdirSync(dir, { recursive: true });
      }

      files.forEach(async (file) => {
        const fileName = path.basename(file.name);

        // Download the file as a buffer
        const [fileContent] = await file.download();

        fs.writeFile(path.join(dir, fileName), fileContent, (err) => {
          if (err) {
            console.error(`Error writing file ${fileName}:`, err);
          } else {
            console.log(`File ${fileName} saved locally.`);
          }
        });
      });
    } catch (err) {
      console.error("Error reading files:", err);
    }
  }

  async loadModel() {
    try {
      const modelPath = path.join(
        __dirname,
        "model/klasifikasiWarna/model.json"
      );
      this.model = await tf.loadLayersModel(`file://${modelPath}`);
      logger.info("Model loaded");
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

  async analyze(image: Buffer) {
    const uint8array = new Uint8Array(image);
    let imageTensor = tf.node.decodeImage(uint8array, 3); // 3 for RGB images

    // Resize the image to the target size
    imageTensor = tf.image.resizeBilinear(imageTensor, [300, 300]);

    // Normalize the pixel values
    imageTensor = imageTensor.div(tf.scalar(255));

    // Add a dimension
    imageTensor = imageTensor.expandDims(0);

    // predict
    const prediction = this.model?.predict(imageTensor) as tf.Tensor;
    const classIndex = tf.argMax(prediction, 1).dataSync()[0];
    const className = this.getClassLabel(classIndex);
    return className;
  }

  getResult = async (req: Request, res: Response, next: NextFunction) => {
    try {
      if (!req.file) {
        res.status(400).send({
          message: "image is required",
        });
      }
      const image = req.file?.buffer;
      const bloodColor = await this.analyze(image as Buffer);
      res.json({
        data: {
          bloodColor,
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
