package com.capstone.feminacare.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetUserProfileResponse(

	@field:SerializedName("data")
	val data: userData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class userData(

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("weight")
	val weight: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("height")
	val height: String? = null
)
