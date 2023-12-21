import JwtMiddleware from "../middlewares/JwtMIddleware";
import BaseRoutes from "./BaseRoutes";
class ArticleRoutes extends BaseRoutes{
  public setRoutes(): void {
      this.routes.get('/',[JwtMiddleware],()=>{})
      this.routes.get("/:id",[JwtMiddleware],()=>{})
  }
}

export default new ArticleRoutes().routes