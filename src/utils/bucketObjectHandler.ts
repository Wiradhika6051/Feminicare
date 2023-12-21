import logger from "./logger";
import fs from "fs";
import storageClient from "./cloudStorageClient";
import path from "path";
import { CONST } from "./constant";

const BASE_PATH = CONST.ENVIRONMENT=='dev' ? '' : '/temp/'

async function cpyBucketFiles(
  bucketName: string,
  prefix: string,
  dest: string
) {
  try {
    const [files] = await storageClient
      .bucket(bucketName)
      .getFiles({ prefix: prefix });

    const dir = BASE_PATH + dest;

    if (!fs.existsSync(dir)) {
      fs.mkdirSync(dir, { recursive: true });
    }

    await Promise.all(
      files.map(async (file) => {
        const fileName = path.basename(file.name);

        // Download the file as a buffer
        const [fileContent] = await file.download();

        fs.writeFile(path.join(dir, fileName), fileContent, (err) => {
          if (err) {
            logger.error(`Error writing file ${fileName}:`, err);
          } else {
            logger.info(`File ${fileName} saved locally.`);
          }
        });
      })
    );
  } catch (err) {
    logger.error("Error reading files:", err);
  }
}

export default cpyBucketFiles;
