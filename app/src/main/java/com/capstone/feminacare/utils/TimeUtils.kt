package com.capstone.feminacare.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimeUtils {
    fun getTimeOfDay() : String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        return when(hour) {
            in 0..11 -> "Morning"
            in 12..17 -> "Afternoon"
            in 18..23 -> "Night"
            else -> "Unknown"
        }
    }

    fun formatMillisToDateTime(millis: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return dateFormat.format(calendar.time)
    }
}