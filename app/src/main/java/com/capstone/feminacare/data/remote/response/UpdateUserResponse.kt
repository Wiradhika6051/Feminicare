package com.capstone.feminacare.data.remote.response

import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String? = null,

	@field:SerializedName("weight")
	val weight: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("height")
	val height: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
