import express,{ Express,Request,Response,Application } from "express";
import dotenv from 'dotenv';
import bodyParser from "body-parser"
import helmet from "helmet"
import BloodAnalyzerRoutes from "./routes/BloodAnalysisRoutes";
//buat ngurus dotenv
dotenv.config()

const app:Application = express();
const port = process.env.port ?? 8080;

//middleware
app.use(bodyParser.json({limit:'50mb'}))
app.use(bodyParser.urlencoded({extended:true}))
app.use(helmet({crossOriginResourcePolicy: false}))

app.use('/api/v1/blood-analysis', BloodAnalyzerRoutes);
//default
app.get('/',(req:Request,res:Response)=>{
  console.log("test request!")
  res.send("Request success!")
})

app.listen(port,()=>{
  console.log(`🌎 Server is listening at port ${port}`)
})
