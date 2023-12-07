import ProfileController from "../controller/ProfileController";
import BaseRoutes from "./BaseRoutes";

class ProfileRoutes extends BaseRoutes{
  public setRoutes(): void {
      this.routes.get('/',ProfileController.getProfile)
  }
}
export default new ProfileRoutes().routes