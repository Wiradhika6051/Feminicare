import { Request, Response, NextFunction } from 'express'
import BaseController from './BaseController'
import logger from '../utils/logger'
class BloodAnalysisController extends BaseController {
  getResult = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const colorIndex = Math.floor(Math.random() * 7)
      res.json({
        data: {
          colorIndex
        },
        message: 'Blood Analysis Finished Sucessfully'
      })
      logger.info(`Blood analysis return ${colorIndex}`)
    } catch (err: unknown) {
      logger.error(err)
      res.status(500).send({
        message: 'internal server error'
      })
    }
  }
}
export default new BloodAnalysisController()
