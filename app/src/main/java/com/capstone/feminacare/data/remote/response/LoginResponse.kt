package com.capstone.feminacare.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val loginData: LoginData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class LoginData(

	@field:SerializedName("userId")
	val userId: String? = null,

)
