import { Storage } from "@google-cloud/storage";
import { CONST } from "./constant";

interface StorageClientConfig {
  [key: string]: string;
}

const config: StorageClientConfig = {
  projectId: CONST.PROJECT_ID as string,
};

if (CONST.ENVIRONMENT === "dev") {
  config.keyFilename = CONST.BUCKET_CREDENTIALS as string;
}

const storageClient = new Storage(config);

export default storageClient;
