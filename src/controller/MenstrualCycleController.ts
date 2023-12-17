import { Request, Response, NextFunction } from "express";
import BaseController from "./BaseController";
import logger from "../utils/logger";
import firestoreClient from "../utils/firestoreClient";
import { Timestamp } from "@google-cloud/firestore";

class MenstrualCycleController extends BaseController {
  constructor() {
    super();
  }

  doesDateBelongToCycle = (date: Timestamp, cycle: any) => {
    // if date has 1 to 2 days difference with either start-date or end-date of cycle, then date belongs to cycle
    const startDate = cycle["start-date"].toDate();
    const endDate = cycle["end-date"].toDate();
    const diffFromStartDate = Math.abs(
      Math.round((date.toDate().getTime() - startDate.getTime()) / 86400000)
    );
    const diffFromEndDate = Math.abs(
      Math.round((date.toDate().getTime() - endDate.getTime()) / 86400000)
    );
    if (diffFromStartDate <= 2 || diffFromEndDate <= 2) {
      return true;
    }
  };

  updateStartWithMinDate = async (cycleRef: any, date: Timestamp) => {
    const cycleToUpdate = cycleRef.docs[0];
    const cycleToUpdateData = cycleToUpdate.data();
    const cycleToUpdateStartDate = cycleToUpdateData["start-date"].toDate();
    if (date.toDate() < cycleToUpdateStartDate) {
      await cycleToUpdate.ref.update({ "start-date": date });
    }
  };

  updateEndWithMaxDate = async (cycleRef: any, date: Timestamp) => {
    const cycleToUpdate = cycleRef.docs[0];
    const cycleToUpdateData = cycleToUpdate.data();
    const cycleToUpdateEndDate = cycleToUpdateData["end-date"].toDate();
    if (date.toDate() > cycleToUpdateEndDate) {
      await cycleToUpdate.ref.update({ "end-date": date });
    }
  };

  getLastCycle = async (userId: string) => {
    const userRef = firestoreClient.collection("users").doc(userId);
    const menstrualCycleRef = userRef.collection("cycles");
    const menstrualCycleSnapshot = await menstrualCycleRef
      .orderBy("end-date", "desc")
      .limit(1)
      .get();
    if (menstrualCycleSnapshot.empty) {
      return null;
    }
    const menstrualCycle = menstrualCycleSnapshot.docs[0].data();
    return menstrualCycle;
  };

  addDailyEntry = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const { id } = req.params;
      let { date } = req.body;
      const userRef = firestoreClient.collection("users").doc(id);
      const usersSnapshot = await userRef.get();
      if (!usersSnapshot.exists) {
        res.status(404).send({ message: "User not found" });
        return;
      }

      date = Timestamp.fromDate(new Date(date));

      // get last cycle
      const lastCycle = await this.getLastCycle(id);
      let cycleNo = lastCycle ? lastCycle.cycleNo : 1;
      let menstrualCycleRef;

      if (!lastCycle) {
        // this is user's first entry
        menstrualCycleRef = userRef.collection("cycles").doc();
        const menstrualCycle = {
          id: menstrualCycleRef.id,
          cycleNo: cycleNo,
          "start-date": date,
          "end-date": date,
          userId: id,
        };
        await menstrualCycleRef.set(menstrualCycle);
      } else {
        const allMenstrualCycleRef = userRef.collection("cycles");

        // find two consecutive cycles A and B in which start-date(A) <= date <= start-date(B)

        // find A
        const cycleARef = await allMenstrualCycleRef
          .where("start-date", "<=", date)
          .orderBy("start-date", "desc")
          .limit(1)
          .get();

        // find B
        const cycleBRef = await allMenstrualCycleRef
          .where("start-date", ">=", date)
          .orderBy("start-date", "asc")
          .limit(1)
          .get();

        // case 1: A does not exist, then either date belongs to B or date is before B
        // case 2: B does not exist, then either date belongs to A or date is after A
        // case 3: A and B exist, then date might belong to A, B or between A and B
        if (cycleARef.empty) {
          // case 1
          console.log("case 1");
          if (!cycleBRef.empty) {
            cycleNo = cycleBRef.docs[0].data().cycleNo;
            if (this.doesDateBelongToCycle(date, cycleBRef.docs[0].data())) {
              await this.updateStartWithMinDate(cycleBRef, date);
              console.log("cycle B");
            } else {
              // update all cycleNo after cycleB
              const cyclesToUpdate = await allMenstrualCycleRef
                .where("cycleNo", ">=", cycleNo)
                .get();
              for (const cycle of cyclesToUpdate.docs) {
                await cycle.ref.update({ cycleNo: cycle.data().cycleNo + 1 });
              }
              console.log("cycle B - 1");
            }
          }
        } else if (cycleBRef.empty) {
          // case 2
          console.log("case 2");
          if (this.doesDateBelongToCycle(date, cycleARef.docs[0].data())) {
            cycleNo = cycleARef.docs[0].data().cycleNo;
            await this.updateEndWithMaxDate(cycleARef, date);
            console.log("cycle A");
          } else {
            cycleNo = cycleARef.docs[0].data().cycleNo + 1;
            console.log("cycle A + 1");
          }
        } else {
          // case 3
          console.log("case 3");
          const cycleA = cycleARef.docs[0].data();
          const cycleB = cycleBRef.docs[0].data();
          if (this.doesDateBelongToCycle(date, cycleA)) {
            cycleNo = cycleA.cycleNo;
            await this.updateEndWithMaxDate(cycleARef, date);
            console.log("cycle A");
          } else if (this.doesDateBelongToCycle(date, cycleB)) {
            cycleNo = cycleB.cycleNo;
            await this.updateStartWithMinDate(cycleBRef, date);
            console.log("cycle B");
          } else {
            cycleNo = cycleA.cycleNo + 1;
            // update all cycleNo after cycleA
            const cyclesToUpdate = await allMenstrualCycleRef
              .where("cycleNo", ">", cycleA.cycleNo)
              .get();
            for (const cycle of cyclesToUpdate.docs) {
              await cycle.ref.update({ cycleNo: cycle.data().cycleNo + 1 });
            }
            console.log("cycle A < cycle < cycle B");
          }
        }
      }

      // check if cycleNo is already exist (date is cycle A or B)
      const cycleRef = await userRef
        .collection("cycles")
        .where("cycleNo", "==", cycleNo)
        .get();

      if (!cycleRef.empty) {
        // update cycle end-date with max date
        menstrualCycleRef = cycleRef.docs[0].ref;
        const cycleToUpdate = cycleRef.docs[0];
        const cycleToUpdateData = cycleToUpdate.data();
        const cycleToUpdateEndDate = cycleToUpdateData["end-date"].toDate();
        if (date.toDate() > cycleToUpdateEndDate) {
          await cycleToUpdate.ref.update({ "end-date": date });
        }
      } else {
        // now we know that date is not cycle A or B
        // create new cycle
        menstrualCycleRef = userRef.collection("cycles").doc();
        const menstrualCycle = {
          id: menstrualCycleRef.id,
          cycleNo: cycleNo,
          "start-date": date,
          "end-date": date,
          userId: id,
        };
        await menstrualCycleRef.set(menstrualCycle);
      }

      // add daily entry
      const dailyEntryRef = menstrualCycleRef.collection("dailyEntries").doc();
      const dailyEntry = {
        id: dailyEntryRef.id,
        date: date,
        userId: id,
      };
      await dailyEntryRef.set(dailyEntry);
      res.json({
        data: {
          cycleNo: cycleNo,
          date: date.toDate(),
        },
        message: "Daily entry added successfully",
      });
      return;
    } catch (error) {
      logger.error(error);
      return next(error);
    }
  };

  getAllDailyEntries = async (
    req: Request,
    res: Response,
    next: NextFunction
  ) => {
    try {
      const { id } = req.params;
      const userRef = firestoreClient.collection("users").doc(id);
      const usersSnapshot = await userRef.get();
      if (!usersSnapshot.exists) {
        res.status(404).send({ message: "User not found" });
        return;
      }

      // get all cycles
      const allMenstrualCycleRef = userRef.collection("cycles");
      const allMenstrualCycleSnapshot = await allMenstrualCycleRef.get();
      if (allMenstrualCycleSnapshot.empty) {
        res.status(404).send({ message: "Cycle not found" });
        return;
      }

      // get all daily entries
      let allDailyEntries = [];
      for (const cycle of allMenstrualCycleSnapshot.docs) {
        const dailyEntriesRef = cycle.ref.collection("dailyEntries");
        const dailyEntriesSnapshot = await dailyEntriesRef.get();
        if (!dailyEntriesSnapshot.empty) {
          for (const dailyEntry of dailyEntriesSnapshot.docs) {
            allDailyEntries.push({
              cycleId: cycle.id,
              id: dailyEntry.id,
              date: dailyEntry.data()["date"].toDate(),
            });
          }
        }
      }

      res.json({
        data: allDailyEntries,
        message: "Get all daily entries success",
      });
      return;
    } catch (error) {
      logger.error(error);
      return next(error);
    }
  };
}

export default new MenstrualCycleController();
