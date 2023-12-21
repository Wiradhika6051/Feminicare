import ProfileController from "../controller/ProfileController";
import JwtUserMiddleware from "../middlewares/JwtUserMIddleware";
import BaseRoutes from "./BaseRoutes";

class ProfileRoutes extends BaseRoutes{
  public setRoutes(): void {
      this.routes.get('/:id',[JwtUserMiddleware],ProfileController.getProfile)
      this.routes.post('/:id',[JwtUserMiddleware],ProfileController.updateProfile)
  }
}
export default new ProfileRoutes().routes