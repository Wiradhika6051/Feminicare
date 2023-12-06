import "dotenv/config";

export const CONST = {
  PORT: process.env.port ?? 8080,
  PROJECT_ID:process.env.PROJECT_ID,
}