import { NextFunction, Request, Response } from 'express'
import BaseController from './BaseController'
import logger from '../utils/logger'
import firestoreClient from '../utils/firestoreClient'

class ArticleController extends BaseController {
  getAllArticles = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const articlesSnapshot = await firestoreClient
        .collection('articles')
        .get()
      const data = articlesSnapshot.docs.map((article) => {
        const articleData = article.data()
        return {
          id: article.id,
          title: articleData.title,
          imageUrl: articleData.imageUrl,
          topic: articleData.topic,
          published_timestamp: articleData.published_timestamp
            .toDate()
            .toISOString()
        }
      })
      res.json({
        data,
        message: 'Health articles retrieved'
      })
    } catch (err: unknown) {
      logger.error(err)
      res.status(500).send({
        message: 'internal server error'
      })
    }
  }

  getArticleById = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const {id} = req.params
      //kalau kosong, udah kehandle di get all
      const articlesSnapshot = await firestoreClient.collection('articles').doc(id).get()
      if(!articlesSnapshot.exists){
        res.status(404).send({
          message:"Article not found"
        })
        return
      }
      logger.info(`Article request for id ${id}`)
      //article ada
      const {title, imageUrl,topic,content,summary,source_link} = articlesSnapshot.data() as any
      res.json({
        data:{
          id: articlesSnapshot.id,
          title,
          imageUrl,
          topic,
          content,
          summary,
          source_link
        },
        message:'Health article retrieved'
      })
    } catch (err: unknown) {
      logger.error(err)
      res.status(500).send({
        message: 'internal server error'
      })
    }
  }
}

export default new ArticleController()
