import MenstrualCycleController from "../controller/MenstrualCycleController";
import JwtPathMiddleware from "../middlewares/JwtPathMIddleware";
import BaseRoutes from "./BaseRoutes";

// TODO: User jwt middleware

class MenstrualCycleRoutes extends BaseRoutes {
  public setRoutes(): void {
    this.routes.post(
      "/daily-entry/:id",
      // [JwtPathMiddleware],
      MenstrualCycleController.addDailyEntry
    );
    this.routes.get(
      "/cycle-history/:id",
      // [JwtPathMiddleware],
      MenstrualCycleController.getAllDailyEntries
    );
    this.routes.get(
      "/cycle-prediction/:id",
      // [JwtPathMiddleware],
      MenstrualCycleController.getNextCyclePrediction
    );
  }
}

export default new MenstrualCycleRoutes().routes;
