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
    // if date has 1 to 2 days difference with either start_date or end_date of cycle, then date belongs to cycle
    const startDate = cycle["start_date"].toDate();
    const endDate = cycle["end_date"].toDate();
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
    const cycleToUpdateStartDate = cycleToUpdateData["start_date"].toDate();
    if (date.toDate() < cycleToUpdateStartDate) {
      await cycleToUpdate.ref.update({ start_date: date });
      const cycleToUpdateEndDate = cycleToUpdateData["end_date"].toDate();
      const cycleLength = Math.abs(
        Math.round(
          (cycleToUpdateEndDate.getTime() - date.toDate().getTime()) / 86400000
        )
      );
      await cycleToUpdate.ref.update({ cycle_length: cycleLength + 1 });
    }
  };

  updateEndWithMaxDate = async (cycleRef: any, date: Timestamp) => {
    const cycleToUpdate = cycleRef.docs[0];
    const cycleToUpdateData = cycleToUpdate.data();
    const cycleToUpdateEndDate = cycleToUpdateData["end_date"].toDate();
    if (date.toDate() > cycleToUpdateEndDate) {
      await cycleToUpdate.ref.update({ end_date: date });
      const cycleToUpdateStartDate = cycleToUpdateData["start_date"].toDate();
      const cycleLength = Math.abs(
        Math.round(
          (date.toDate().getTime() - cycleToUpdateStartDate.getTime()) /
            86400000
        )
      );
      await cycleToUpdate.ref.update({ cycle_length: cycleLength + 1 });
    }
  };

  getLastCycle = async (user_id: string) => {
    const userRef = firestoreClient.collection("users").doc(user_id);
    const menstrualCycleRef = userRef.collection("cycles");
    const menstrualCycleSnapshot = await menstrualCycleRef
      .orderBy("end_date", "desc")
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
      let cycleNo = lastCycle ? lastCycle.cycle_no : 1;
      let menstrualCycleRef;

      if (!lastCycle) {
        // this is user's first entry
        menstrualCycleRef = userRef.collection("cycles").doc();
        const menstrualCycle = {
          id: menstrualCycleRef.id,
          cycle_no: cycleNo,
          start_date: date,
          end_date: date,
          cycle_length: 1,
          user_id: id,
        };
        await menstrualCycleRef.set(menstrualCycle);
      } else {
        const allMenstrualCycleRef = userRef.collection("cycles");

        // find two consecutive cycles A and B in which start_date(A) <= date <= start_date(B)

        // find A
        const cycleARef = await allMenstrualCycleRef
          .where("start_date", "<=", date)
          .orderBy("start_date", "desc")
          .limit(1)
          .get();

        // find B
        const cycleBRef = await allMenstrualCycleRef
          .where("start_date", ">=", date)
          .orderBy("start_date", "asc")
          .limit(1)
          .get();

        // case 1: A does not exist, then either date belongs to B or date is before B
        // case 2: B does not exist, then either date belongs to A or date is after A
        // case 3: A and B exist, then date might belong to A, B or between A and B
        if (cycleARef.empty) {
          // case 1
          console.log("case 1");
          if (!cycleBRef.empty) {
            cycleNo = cycleBRef.docs[0].data().cycle_no;
            if (this.doesDateBelongToCycle(date, cycleBRef.docs[0].data())) {
              await this.updateStartWithMinDate(cycleBRef, date);
              console.log("cycle B");
            } else {
              // update all cycle_no after cycleB
              const cyclesToUpdate = await allMenstrualCycleRef
                .where("cycle_no", ">=", cycleNo)
                .get();
              for (const cycle of cyclesToUpdate.docs) {
                await cycle.ref.update({ cycle_no: cycle.data().cycle_no + 1 });
              }
              console.log("cycle B - 1");
            }
          }
        } else if (cycleBRef.empty) {
          // case 2
          console.log("case 2");
          if (this.doesDateBelongToCycle(date, cycleARef.docs[0].data())) {
            cycleNo = cycleARef.docs[0].data().cycle_no;
            await this.updateEndWithMaxDate(cycleARef, date);
            console.log("cycle A");
          } else {
            cycleNo = cycleARef.docs[0].data().cycle_no + 1;
            console.log("cycle A + 1");
          }
        } else {
          // case 3
          console.log("case 3");
          const cycleA = cycleARef.docs[0].data();
          const cycleB = cycleBRef.docs[0].data();
          if (this.doesDateBelongToCycle(date, cycleA)) {
            cycleNo = cycleA.cycle_no;
            await this.updateEndWithMaxDate(cycleARef, date);
            console.log("cycle A");
          } else if (this.doesDateBelongToCycle(date, cycleB)) {
            cycleNo = cycleB.cycle_no;
            await this.updateStartWithMinDate(cycleBRef, date);
            console.log("cycle B");
          } else {
            cycleNo = cycleA.cycle_no + 1;
            // update all cycle_no after cycleA
            const cyclesToUpdate = await allMenstrualCycleRef
              .where("cycle_no", ">", cycleA.cycle_no)
              .get();
            for (const cycle of cyclesToUpdate.docs) {
              await cycle.ref.update({ cycle_no: cycle.data().cycle_no + 1 });
            }
            console.log("cycle A < cycle < cycle B");
          }
        }
      }

      // check if cycle_no is already exist (date is cycle A or B)
      const cycleRef = await userRef
        .collection("cycles")
        .where("cycle_no", "==", cycleNo)
        .get();

      if (!cycleRef.empty) {
        // update cycle end_date with max date
        menstrualCycleRef = cycleRef.docs[0].ref;
        const cycleToUpdate = cycleRef.docs[0];
        const cycleToUpdateData = cycleToUpdate.data();
        const cycleToUpdateEndDate = cycleToUpdateData["end_date"].toDate();
        if (date.toDate() > cycleToUpdateEndDate) {
          await cycleToUpdate.ref.update({ end_date: date });
        }
      } else {
        // now we know that date is not cycle A or B
        // create new cycle
        menstrualCycleRef = userRef.collection("cycles").doc();
        const menstrualCycle = {
          id: menstrualCycleRef.id,
          cycle_no: cycleNo,
          start_date: date,
          end_date: date,
          user_id: id,
          cycle_length: 1,
        };
        await menstrualCycleRef.set(menstrualCycle);
      }

      // add daily entry
      const dailyEntryRef = menstrualCycleRef.collection("daily_entries").doc();
      const dailyEntry = {
        id: dailyEntryRef.id,
        date: date,
        user_id: id,
      };
      await dailyEntryRef.set(dailyEntry);
      res.json({
        data: {
          cycle_no: cycleNo,
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
        const dailyEntriesRef = cycle.ref.collection("daily_entries");
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
