package com.capstone.feminacare.data.remote.response

import com.google.gson.annotations.SerializedName

data class BloodAnalysisResponse(

    @field:SerializedName("data")
	val data: Data,

    @field:SerializedName("message")
	val message: String
)

data class Data(

	@field:SerializedName("colorIndex")
	val colorIndex: Int
)
