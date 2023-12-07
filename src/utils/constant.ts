import "dotenv/config";

export const CONST = {
  PORT: process.env.port ?? 8080,
  PROJECT_ID:process.env.PROJECT_ID,
  ENVIRONMENT:process.env.ENVIRONMENT,
  KEY_PATH:process.env.KEY_PATH,
  SECRET:process.env.SECRET,
}