import { Router } from "express";
abstract class BaseRoutes {
  public routes: Router;

  constructor() {
    this.routes = Router()
    this.setRoutes()
  }

  abstract setRoutes(): void;
}

export default BaseRoutes;