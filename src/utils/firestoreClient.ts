import { Firestore } from "@google-cloud/firestore";

const firestoreClient = new Firestore({projectId:process.env.PROJECT_ID})

export default firestoreClient