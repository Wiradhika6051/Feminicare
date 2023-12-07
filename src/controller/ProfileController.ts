import { Request,Response,NextFunction } from "express";
import BaseController from "./BaseController";
import logger from "../utils/logger";
import firestoreClient from "../utils/firestoreClient";
import User from "../interfaces/users";

class ProfileController extends BaseController{
  getProfile = async (req:Request,res:Response,next:NextFunction) => {
    try{
      //tes client
      logger.info("Profile Request For User X")
      const usersSnapshot = await firestoreClient.collection('users').get()
      const datas: User[] = usersSnapshot.docs.map((doc) => ({
        id: doc.id,
        data: doc.data() as any,
      }));
      const data = datas.map((data)=>{return {
        username: data.data.username,
        first_name: data.data.first_name,
        last_name: data.data.last_name,
        email: data.data.email,
        age: data.data.age,
        weight: data.data.weight
      }})
      res.json({
        data,       
        message: "User profile retrieved"
      })
    }catch(err:unknown){
      logger.error(err)
      res.status(500).send({
        message: 'internal server error'
      })
    }
  }
}

export default new ProfileController();