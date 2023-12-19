import express, { Express, Request, Response, Application } from "express";
import dotenv from "dotenv";
import bodyParser from "body-parser";
import helmet from "helmet";
import BloodAnalyzerRoutes from "./routes/BloodAnalysisRoutes";
import ProfileRoutes from "./routes/ProfileRoutes";
import { CONST } from "./utils/constant";
import AuthenticationRoutes from "./routes/AuthenticationRoutes";
import MenstrualCycleRoutes from "./routes/MenstrualCycleRoutes";

//buat ngurus dotenv
dotenv.config();
const app: Application = express();
const port = CONST.PORT;

//middleware
app.use(bodyParser.json({ limit: "50mb" }));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(helmet({ crossOriginResourcePolicy: false }));

app.use("/api/v1/profile", ProfileRoutes);
app.use("/api/v1/blood-analysis", BloodAnalyzerRoutes);
app.use("/api/v1/auth", AuthenticationRoutes);
app.use("/api/v1/menstrual-cycle", MenstrualCycleRoutes);

//default
app.get("/", (req: Request, res: Response) => {
  res.send("Request success!");
});

app.listen(port, () => {
  console.log(`🌎 Server is listening at port ${port}`);
});
