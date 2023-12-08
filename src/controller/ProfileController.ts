import { Request,Response,NextFunction } from "express";
import BaseController from "./BaseController";
import logger from "../utils/logger";
import firestoreClient from "../utils/firestoreClient";
import User from "../interfaces/users";

class ProfileController extends BaseController{
  getProfile = async (req:Request,res:Response,next:NextFunction) => {
    try{
      const {id} = req.params
      logger.info(`Profile Request For User ${id}`)
      const usersSnapshot = await firestoreClient.collection('users').doc(id).get()
      const user: User = {
        id: usersSnapshot.id,
        data: usersSnapshot.data() as any,
      };
      const data =  {
        username: user.data.username,
        first_name: user.data.first_name,
        last_name: user.data.last_name,
        email: user.data.email,
        date_of_birth: user.data.date_of_birth,
        weight: user.data.weight
      }
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