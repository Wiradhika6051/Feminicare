import { Request, Response, NextFunction } from 'express'
import BaseController from './BaseController'
import logger from '../utils/logger'
import firestoreClient from '../utils/firestoreClient'
import User from '../interfaces/users'
import { Filter, Timestamp } from '@google-cloud/firestore'
import * as bcrypt from 'bcrypt'

class ProfileController extends BaseController {
  getProfile = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const { id } = req.params
      logger.info(`Profile Request For User ${id}`)
      const usersSnapshot = await firestoreClient
        .collection('users')
        .doc(id)
        .get()
      const user: User = {
        id: usersSnapshot.id,
        data: usersSnapshot.data() as any
      }
      const data = {
        username: user.data.username,
        first_name: user.data.first_name,
        last_name: user.data.last_name,
        email: user.data.email,
        date_of_birth: user.data.date_of_birth,
        weight: user.data.weight
      }
      res.json({
        data,
        message: 'User profile retrieved'
      })
    } catch (err: unknown) {
      logger.error(err)
      res.status(500).send({
        message: 'internal server error'
      })
    }
  }

  updateProfile = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const { id } = req.params
      const data = req.body
      const userRef = await firestoreClient.collection('users').doc(id)
      const usersSnapshot = await userRef.get()
      if (!usersSnapshot.exists) {
        res.status(404).send({ message: 'User not found' })
      }
      const existingData = usersSnapshot.data() as User
      //proses data yang sensitif
      if (data.date_of_birth) {
        data.date_of_birth = Timestamp.fromDate(new Date(data.date_of_birth))
      }
      if (data.username || data.email) {
        //pastiin unik
        const existingUser = await firestoreClient
          .collection('users')
          .where(
            Filter.or(
              Filter.where(
                'username',
                '==',
                data.username ?? existingData!!.data.username
              ),
              Filter.where(
                'email',
                '==',
                data.email ?? existingData!!.data.email
              )
            )
          )
          .get()
        if (!existingUser.empty) {
          //kalau dah ada, gak bisa register pakai username ini
          res.status(409).send({
            message: `username or email already exist`
          })
          return
        }
      }
      //konversi ke angka
      if (data.weight) {
        //cek jika NaN
        if (isNaN(data.weight)) {
          res.status(400).send({
            message: 'Invalid input'
          })
        }
        data.weight = Number(data.weight)
      }
      if (data.height) {
        //cek jika NaN
        if (isNaN(data.height)) {
          res.status(400).send({
            message: 'Invalid input'
          })
        }
        data.height = Number(data.height)
      }
      //hash password
      if (data.password) {
        data.password = await bcrypt.hash(
          data.password,
          existingData!!.data.salt
        )
      }
      await userRef.update(data)
      logger.info(`Profile Modification Request For User ${id}`)
      res.send({ message: 'Profile updated' })
    } catch (err: unknown) {
      logger.error(err)
      res.status(500).send({
        message: 'internal server error'
      })
    }
  }
}

export default new ProfileController()
