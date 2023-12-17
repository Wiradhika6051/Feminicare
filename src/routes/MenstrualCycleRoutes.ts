import MenstrualCycleController from "../controller/MenstrualCycleController";
import JwtPathMiddleware from "../middlewares/JwtPathMIddleware";
import BaseRoutes from "./BaseRoutes";

// TODO: User jwt middleware

class MenstrualCycleRoutes extends BaseRoutes {
  public setRoutes(): void {
    this.routes.post(
      "/daily-entry/:id",
      // [JwtPathMiddleware],
      MenstrualCycleController.addMenstrualCycle
    );
    this.routes.get(
      "/cycle-history/:id",
      // [JwtPathMiddleware],
      MenstrualCycleController.getMenstrualCycleHistory
    );
  }
}

export default new MenstrualCycleRoutes().routes;
