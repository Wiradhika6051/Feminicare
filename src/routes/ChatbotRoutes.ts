import ChatbotController from "../controller/ChatbotController";
import BaseRoutes from "./BaseRoutes";

// TODO: User jwt middleware

class ChatbotRoutes extends BaseRoutes {
  public setRoutes(): void {
    this.routes.post("/", ChatbotController.getResponse);
  }
}

export default new ChatbotRoutes().routes;
