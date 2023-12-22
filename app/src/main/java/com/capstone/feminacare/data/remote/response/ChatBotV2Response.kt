package com.capstone.feminacare.data.remote.response

import com.google.gson.annotations.SerializedName

data class ChatBotV2Response(

	@field:SerializedName("data")
	val data: DataBot? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataBot(

	@field:SerializedName("response")
	val response: String? = null
)
