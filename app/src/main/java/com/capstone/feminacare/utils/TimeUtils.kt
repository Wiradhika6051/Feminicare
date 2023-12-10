package com.capstone.feminacare.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs

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

    fun getTimeAgo(dateString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val date = dateFormat.parse(dateString)
            val currentTime = System.currentTimeMillis()
            val timeDifference = abs(currentTime - (date?.time ?: 0))

            val seconds = timeDifference / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val months = days / 30
            val years = days / 365

            return when {
                years > 1 -> "$years years ago"
                months > 1 -> "$months months ago"
                days > 1 -> "$days days ago"
                hours > 1 -> "$hours hours ago"
                minutes > 1 -> "$minutes minutes ago"
                else -> "just now"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "Invalid date"
        }
    }

    fun timeMillisToCalendar(millis: Long) : Calendar {
        val date = Date(millis)

        val calendar = Calendar.getInstance()
        calendar.time = date

        return calendar
    }
}