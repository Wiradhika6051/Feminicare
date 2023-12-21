import MenstrualCycleController from "../controller/MenstrualCycleController";
import JwtUserMiddleware from "../middlewares/JwtUserMIddleware";
import BaseRoutes from "./BaseRoutes";

// TODO: User jwt middleware

class MenstrualCycleRoutes extends BaseRoutes {
  public setRoutes(): void {
    this.routes.post(
      "/daily-entry/:id",
      // [JwtUserMiddleware],
      MenstrualCycleController.addDailyEntry
    );
    this.routes.get(
      "/cycle-history/:id",
      // [JwtUserMiddleware],
      MenstrualCycleController.getAllDailyEntries
    );
    this.routes.get(
      "/cycle-prediction/:id",
      // [JwtUserMiddleware],
      MenstrualCycleController.getNextCyclePrediction
    );
  }
}

export default new MenstrualCycleRoutes().routes;
