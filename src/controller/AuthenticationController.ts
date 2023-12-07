import { NextFunction, Request, Response } from 'express'
import BaseController from './BaseController'
import logger from '../utils/logger'
import firestoreClient from '../utils/firestoreClient'
import * as bcrypt from 'bcrypt'
import JwtPayload from '../interfaces/JwtPayload'
import jwt from 'jsonwebtoken'
import { CONST } from '../utils/constant'

class AuthenticationController extends BaseController {
  login = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const { username, password } = req.body
      if (!username || !password) {
        res.status(400).send({
          message: 'invalid username and/or password'
        })
      }
      //buat dokumen baru
      const docRef = firestoreClient.collection('users').doc()
      await docRef.set({})
      // res.header().json({
      //   message: 'Login successful'
      // })
      // logger.info(`Blood analysis return ${colorIndex}`)
    } catch (err: unknown) {
      logger.error(err)
      res.status(500).send({
        message: 'internal server error'
      })
    }
  }
  register = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const { username, email, password, first_name, last_name } = req.body
      //kalau username/password gak ada, kena 400
      if (!username || !password) {
        res
          .status(400)
          .send({ message: 'username and/or password not included' })
      }
      //bikin dokumen baru
      const docRef = firestoreClient.collection('users').doc()
      //hash password
      //generate salt
      const salt = await bcrypt.genSalt()
      //hash it
      const hashedPassword = await bcrypt.hash(password, salt)
      //store user
      const createInfo = await docRef.set({
        username,
        email,
        password: hashedPassword,
        first_name,
        last_name
      })
      logger.info(
        `User ${username} created at ${createInfo.writeTime.toDate()}`
      )
      //buat jwt token
      //buat payload
      const jwtPayload: JwtPayload = {
        id: docRef.id,
        username
      }
      logger.warn(jwtPayload)
      //sign token
      const token = jwt.sign(jwtPayload, CONST.SECRET as string)
      //kirim hasil
      res.cookie('cookie', token, {
        httpOnly: true,
        secure: true
      })
      res.send({
        message: 'Register account successful'
      })
    } catch (err: unknown) {
      logger.error(err)
      res.status(500).send({
        message: 'internal server error'
      })
    }
  }
}
export default new AuthenticationController()
