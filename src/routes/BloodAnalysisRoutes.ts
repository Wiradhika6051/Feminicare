import BloodAnalysisController from "../controller/BloodAnalysisController";
import BaseRoutes from "./BaseRoutes";

class BloodAnalyzerRoutes extends BaseRoutes {
  public setRoutes(): void {
    this.routes.post('/',BloodAnalysisController.getResult)
  }
}
export default new BloodAnalyzerRoutes().routes;