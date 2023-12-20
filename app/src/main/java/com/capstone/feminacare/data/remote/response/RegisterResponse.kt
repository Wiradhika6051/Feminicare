package com.capstone.feminacare.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: RegistData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class RegistData(

	@field:SerializedName("userId")
	val userId: String? = null
)
