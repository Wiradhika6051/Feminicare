import ProfileController from "../controller/ProfileController";
import JwtPathMiddleware from "../middlewares/JwtPathMIddleware";
import BaseRoutes from "./BaseRoutes";

class ProfileRoutes extends BaseRoutes{
  public setRoutes(): void {
      this.routes.get('/:id',[JwtPathMiddleware],ProfileController.getProfile)
      this.routes.post('/:id',[JwtPathMiddleware],ProfileController.updateProfile)
  }
}
export default new ProfileRoutes().routes