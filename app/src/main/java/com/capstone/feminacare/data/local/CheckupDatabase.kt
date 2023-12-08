package com.capstone.feminacare.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BloodCheckup::class], version = 1)
abstract class CheckupDatabase : RoomDatabase() {
    abstract fun checkupDao(): BloodCheckupDao

    companion object {

        @Volatile
        private var INSTANCE: CheckupDatabase? = null

        fun getInstance(context: Context): CheckupDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CheckupDatabase::class.java,
                    "checkup.db"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}