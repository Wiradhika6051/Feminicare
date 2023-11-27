import express,{ Express,Request,Response,Application } from "express";
import dotenv from 'dotenv';

//buat ngurus dotenv
dotenv.config()

const app:Application = express();
const port = process.env.port ?? 8080;


app.get('/',(req:Request,res:Response)=>{
  res.send("Halo Dunia! Masuk Kawan")
})

app.listen(port,()=>{
  console.log(`🌎 Server is listening at port ${port}`)
})
