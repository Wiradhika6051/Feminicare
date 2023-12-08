import { NextFunction, Request, Response } from "express"
import logger from "../utils/logger";
import jwt from "jsonwebtoken"
import { CONST } from "../utils/constant";
import JwtPayload from "../interfaces/JwtPayload";

const JwtPathMiddleware = (req:Request,res:Response,next:NextFunction) => {
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
    //pastikan id nya sama dengan yg di jwt
    if(decoded.id!==req.params.id){
      res.status(403).send({message:"forbidden to get this user information"})
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

export default JwtPathMiddleware;