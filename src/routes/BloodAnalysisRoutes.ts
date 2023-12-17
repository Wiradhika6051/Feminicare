import BloodAnalysisController from "../controller/BloodAnalysisController";
import BaseRoutes from "./BaseRoutes";
import { uploadToMemory } from "../middlewares/multer";

class BloodAnalyzerRoutes extends BaseRoutes {
  public setRoutes(): void {
    this.routes.post(
      "/",
      uploadToMemory.single("menstrualBloodImage"),
      BloodAnalysisController.getResult
    );
  }
}
export default new BloodAnalyzerRoutes().routes;
