package com.capstone.feminacare.data.remote.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class MenstrualCycleResponse(

    @field:SerializedName("cycleNo")
    val cycleNo: Int,

    @field:SerializedName("startDate")
    val startDate: LocalDate,

    @field:SerializedName("endDate")
    val endDate: LocalDate,

    @field:SerializedName("cycleLength")
    val cycleLength: Int,
)

