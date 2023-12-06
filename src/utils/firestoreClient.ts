import { Firestore } from "@google-cloud/firestore";
import { CONST } from "./constant";

const firestoreClient = new Firestore({projectId:CONST.PROJECT_ID})

export default firestoreClient