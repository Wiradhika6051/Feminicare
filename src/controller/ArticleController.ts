import { NextFunction, Request, Response } from "express";
import BaseController from "./BaseController";
import logger from "../utils/logger";
import firestoreClient from "../utils/firestoreClient";

class ArticleController extends BaseController {
  getAllArticles = async (req:Request,res:Response,next:NextFunction) =>{
    try{
      const articlesSnapshot = await firestoreClient.collection('articles').get()
      const data = articlesSnapshot.docs.map((article)=>{
        const articleData = article.data()
        return {
          id: article.id,
          title: articleData.title,
          imageUrl: articleData.imageUrl,
          topic: articleData.topic,
          published_timestamp:articleData.published_timestamp.toDate().toISOString()
        }
      })
      res.json({
        data,
        message: 'Health articles retrieved'
      })
    }catch(err:unknown){
      logger.error(err);
      res.status(500).send({
        message: "internal server error",
      });
    }
  }
}

export default new ArticleController()