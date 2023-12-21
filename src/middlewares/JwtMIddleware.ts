import { NextFunction, Request, Response } from "express"
import logger from "../utils/logger";
import jwt from "jsonwebtoken"
import { CONST } from "../utils/constant";
import JwtPayload from "../interfaces/JwtPayload";

const JwtMiddleware = (req:Request,res:Response,next:NextFunction) => {
  try{
    const accessToken = req.headers.authorization?.split(' ')[1]
    if(!accessToken){
      res.status(401).send({message:"token not found"})
      return
    }
    //decode
    const decoded = jwt.verify(accessToken,CONST.SECRET as string,{algorithms:['HS256']}) as JwtPayload
    if(!decoded){
      res.status(401).send({message:"invalid token"})
      return      
    }
    //ke controller-nya
    return next();
  }catch (err: unknown) {
    logger.error(err)
    res.status(500).send({
      message: 'internal server error'
    })
  }
}

export default JwtMiddleware;