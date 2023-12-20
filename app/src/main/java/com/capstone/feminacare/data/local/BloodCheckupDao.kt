package com.capstone.feminacare.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BloodCheckupDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCheckup(checkup: BloodCheckup)

    @Query("SELECT * FROM blood_checkup ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun getCheckupHistory(limit: Int, offset: Int): List<BloodCheckup>

    @Query("SELECT * FROM blood_checkup ORDER BY timestamp DESC")
    fun getLiveCheckupHistory(): LiveData<List<BloodCheckup>>
}