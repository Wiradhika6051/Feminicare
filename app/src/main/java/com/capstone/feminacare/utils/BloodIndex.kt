package com.capstone.feminacare.utils

import com.capstone.feminacare.R

enum class ColorIndex(val index: Int) {
    GREY(1),
    BROWN(2),
    BLACK(3),
    PINK(4),
    ORANGE(5),
    RED(6);

    companion object {
        fun fromInt(index: Int): ColorIndex? {
            return values().find { it.index == index }
        }
    }
}


data class BloodIndex(
    val colorIndex: ColorIndex,
    var isHealthy: Boolean? = null
) {

    init {
        isHealthy = when(colorIndex) {
            ColorIndex.GREY -> false
            ColorIndex.BROWN -> true
            ColorIndex.BLACK -> true
            ColorIndex.PINK -> true
            ColorIndex.ORANGE -> false
            ColorIndex.RED -> false
        }
    }

    fun getDescription(): Int {
        return when (colorIndex) {
            ColorIndex.GREY -> R.string.blackBlood
            ColorIndex.BROWN -> R.string.brownBlood
            ColorIndex.BLACK -> R.string.blackBlood
            ColorIndex.PINK -> R.string.pinkBlood
            ColorIndex.ORANGE -> R.string.orangeBlood
            ColorIndex.RED -> R.string.redBlood
            }
        }
    }

