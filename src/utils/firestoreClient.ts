import { Firestore } from "@google-cloud/firestore";
import { CONST } from "./constant";

interface FirestoreClientConfig {
  [key:string]: string;
}

const config:FirestoreClientConfig = {
  projectId:CONST.PROJECT_ID as string
}
if(CONST.ENVIRONMENT==="dev"){
  config.keyFilename = CONST.KEY_PATH as string
}

const firestoreClient =  new Firestore(config)

export default firestoreClient