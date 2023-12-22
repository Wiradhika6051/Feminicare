import BloodAnalysisController from "../controller/BloodAnalysisController";
import BaseRoutes from "./BaseRoutes";
import { uploadToMemory } from "../middlewares/multer";
import JwtMiddleware from "../middlewares/JwtMIddleware";

class BloodAnalyzerRoutes extends BaseRoutes {
  public setRoutes(): void {
    this.routes.post(
      "/",
      [JwtMiddleware, uploadToMemory.single("menstrualBloodImage")],
      BloodAnalysisController.getResult
    );
  }
}
export default new BloodAnalyzerRoutes().routes;
