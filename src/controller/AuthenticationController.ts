import { NextFunction, Request, Response } from 'express'
import BaseController from './BaseController'
import logger from '../utils/logger'
import firestoreClient from '../utils/firestoreClient'
import * as bcrypt from 'bcrypt'
import JwtPayload from '../interfaces/JwtPayload'
import jwt from 'jsonwebtoken'
import { CONST } from '../utils/constant'
import { Filter, Timestamp } from '@google-cloud/firestore'

class AuthenticationController extends BaseController {
  login = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const { username, password } = req.body
      if (!username || !password) {
        res.status(400).send({
          message: 'invalid username and/or password'
        })
        logger.warn(`Invalid login attempt at ${new Date().toISOString()}`)
        return;
      }
      //cek apakah ada username dan password tersebut
      const usersRef = firestoreClient.collection('users')
      const snapshot = await usersRef.where('username', '==', username).get()
      if (
        snapshot.empty ||
        (await bcrypt.hash(password, snapshot.docs[0].data().salt)) !==
          snapshot.docs[0].data().password
      ) {
        res.status(400).send({ message: 'invalid username and/or password' })
        logger.warn(`Invalid login attempt at ${new Date().toISOString()}`)
        return;
      }
      logger.info(
        `User [${username}] login at ${new Date().toISOString()}`
      )
      //buat jwt token
      //buat payload
      const jwtPayload: JwtPayload = {
        id: snapshot.docs[0].id,
        username
      }
      //sign token
      const token = jwt.sign(jwtPayload, CONST.SECRET as string)
      //kirim hasil
      res.cookie('cookie', token, {
        httpOnly: true,
        secure: true
      })
      res.send({
        message: 'login successful',
        data:{
          userId:snapshot.docs[0].id
        },
      })
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
      //cek apakah username dan email sudah ada
      const existingUser = await firestoreClient.collection('users').where(Filter.or(
        Filter.where('username','==',username),
        Filter.where('email','==',email)
      )).get()
      if(!existingUser.empty){
        //kalau dah ada, gak bisa register pakai username ini
        res.status(409).send({
          "message": `username or email already exist`
        })
        return
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
        last_name,
        salt,
        register_date:Timestamp.fromDate(new Date())
      })
      logger.info(
        `User [${username}] created at ${createInfo.writeTime.toDate().toISOString()}`
      )
      //buat jwt token
      //buat payload
      const jwtPayload: JwtPayload = {
        id: docRef.id,
        username
      }
      //sign token
      const token = jwt.sign(jwtPayload, CONST.SECRET as string)
      //kirim hasil
      res.cookie('cookie', token, {
        httpOnly: true,
        secure: true
      })
      res.send({
        message: 'Register account successful',
        data:{
          userId:docRef.id
        },
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
