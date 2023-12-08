package com.capstone.feminacare.utils

object StringUtils {

    fun String.limit(maxLength: Int): String {
        return if (this.length <= maxLength) {
            this
        } else {
            this.substring(0, maxLength) + "..."
        }
    }

}