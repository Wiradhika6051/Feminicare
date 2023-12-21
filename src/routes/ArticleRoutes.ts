import ArticleController from "../controller/ArticleController";
import JwtMiddleware from "../middlewares/JwtMIddleware";
import BaseRoutes from "./BaseRoutes";
class ArticleRoutes extends BaseRoutes{
  public setRoutes(): void {
      this.routes.get('/',[JwtMiddleware],ArticleController.getAllArticles)
      this.routes.get("/:id",[JwtMiddleware],ArticleController.getArticleById)
  }
}

export default new ArticleRoutes().routes