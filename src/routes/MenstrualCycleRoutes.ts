import MenstrualCycleController from "../controller/MenstrualCycleController";
import BaseRoutes from "./BaseRoutes";
import JwtUserMiddleware from "../middlewares/JwtUserMIddleware";

class MenstrualCycleRoutes extends BaseRoutes {
  public setRoutes(): void {
    this.routes.post(
      "/daily-entry/:id",
      [JwtUserMiddleware],
      MenstrualCycleController.addDailyEntry
    );
    this.routes.get(
      "/cycle-history/:id",
      [JwtUserMiddleware],
      MenstrualCycleController.getAllDailyEntries
    );
    this.routes.get(
      "/cycle-prediction/:id",
      [JwtUserMiddleware],
      MenstrualCycleController.getNextCyclePrediction
    );
  }
}

export default new MenstrualCycleRoutes().routes;
