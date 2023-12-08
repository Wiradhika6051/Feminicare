package com.capstone.feminacare.data.remote.retrofit

import com.capstone.feminacare.data.remote.response.ArticleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RapidApiService {

//    topic-search?languages=EN&search=Woman%20Menstrual
    @GET("topic-search")
    suspend fun getArticles(
        @Query("languages") languages: String = "EN",
        @Query("search") query: String
    ) : ArticleResponse

}
