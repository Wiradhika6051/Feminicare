package com.capstone.feminacare.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blood_checkup")
data class BloodCheckup(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int,
    @ColumnInfo("timestamp")
    val timeStamp: Long,
    @ColumnInfo("healthInfo")
    val healthInfo: String,
    @ColumnInfo("description")
    val description: String
)
