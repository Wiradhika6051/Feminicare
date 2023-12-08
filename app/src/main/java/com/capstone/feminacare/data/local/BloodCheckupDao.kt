package com.capstone.feminacare.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BloodCheckupDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCheckup(checkup: BloodCheckup)

    @Query("SELECT * FROM blood_checkup ORDER BY timestamp DESC")
    fun getCheckupHistory(): List<BloodCheckup>
}