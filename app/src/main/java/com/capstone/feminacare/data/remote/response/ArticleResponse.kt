package com.capstone.feminacare.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize



data class ArticleResponse(

	@field:SerializedName("result")
	val result: Result,

	@field:SerializedName("news")
	val news: List<NewsItem>
)

@Parcelize
data class NewsItem(

	@field:SerializedName("PublishedOn")
	val publishedOn: String,

	@field:SerializedName("Description")
	val description: String,

	@field:SerializedName("Title")
	val title: String,

	@field:SerializedName("Summary")
	val summary: String,

	@field:SerializedName("Image")
	val image: String,

	@field:SerializedName("Source")
	val source: String,

	@field:SerializedName("Url")
	val url: String,

) : Parcelable

data class Result(

	@field:SerializedName("response")
	val response: String,

	@field:SerializedName("newsCount")
	val newsCount: Int,

	@field:SerializedName("skipped")
	val skipped: Int
)

