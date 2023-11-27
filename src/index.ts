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

//routing
// const router = express.Router()
// app.use(router)

app.use('/api/v1/blood-analysis', BloodAnalyzerRoutes);
//default
app.get('/',(req:Request,res:Response)=>{
  res.send("Request success!")
})

app.listen(port,()=>{
  console.log(`🌎 Server is listening at port ${port}`)
})
