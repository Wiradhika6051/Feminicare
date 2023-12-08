import AuthenticationController from "../controller/AuthenticationController";
import BaseRoutes from "./BaseRoutes";

class AuthenticationRoutes extends BaseRoutes{
  setRoutes(): void {
      this.routes.post('/register',AuthenticationController.register)
      this.routes.post('/login',AuthenticationController.login)
  }
}
export default new AuthenticationRoutes().routes