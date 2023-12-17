import { Request, Response, NextFunction } from "express";
import BaseController from "./BaseController";
import logger from "../utils/logger";
import firestoreClient from "../utils/firestoreClient";
import { Timestamp } from "@google-cloud/firestore";

class MenstrualCycleController extends BaseController {
  constructor() {
    super();
  }

  public async addMenstrualCycle(
    req: Request,
    res: Response,
    next: NextFunction
  ) {
    try {
      const { id } = req.params;
      let { date } = req.body;
      const userRef = firestoreClient.collection("users").doc(id);
      const usersSnapshot = await userRef.get();
      if (!usersSnapshot.exists) {
        res.status(404).send({ message: "User not found" });
        return;
      }

      const menstrualCycleRef = userRef.collection("menstrual-cycle").doc();
      date = Timestamp.fromDate(new Date(date));
      const menstrualCycle = {
        id: menstrualCycleRef.id,
        date: date,
        userId: id,
      };
      await menstrualCycleRef.set(menstrualCycle);
      return res.status(200).json(menstrualCycle);
    } catch (error) {
      logger.error(error);
      return next(error);
    }
  }

  public async getMenstrualCycleHistory(
    req: Request,
    res: Response,
    next: NextFunction
  ) {
    try {
      const { id } = req.params;
      const userRef = firestoreClient.collection("users").doc(id);
      const usersSnapshot = await userRef.get();
      if (!usersSnapshot.exists) {
        res.status(404).send({ message: "User not found" });
        return;
      }

      const menstrualCycleRef = userRef.collection("menstrual-cycle");
      const menstrualCycleSnapshot = await menstrualCycleRef.get();
      const menstrualCycle: any[] = [];
      menstrualCycleSnapshot.forEach((doc) => {
        menstrualCycle.push({
          id: doc.id,
          date: doc.data().date.toDate(),
          userId: doc.data().userId,
        });
      });
      return res.status(200).json(menstrualCycle);
    } catch (error) {
      logger.error(error);
      return next(error);
    }
  }
}

export default new MenstrualCycleController();
