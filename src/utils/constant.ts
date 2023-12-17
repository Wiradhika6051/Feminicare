import "dotenv/config";

export const CONST = {
  PORT: process.env.port ?? 8080,
  PROJECT_ID: process.env.PROJECT_ID,
  ENVIRONMENT: process.env.ENVIRONMENT,
  KEY_PATH: process.env.KEY_PATH,
  SECRET: process.env.SECRET,
  BUCKET_CREDENTIALS: process.env.BUCKET_CREDENTIALS,
  ML_BUCKET_NAME: process.env.ML_BUCKET_NAME,
};
