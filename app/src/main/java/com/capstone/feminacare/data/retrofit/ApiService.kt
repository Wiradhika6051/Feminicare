package com.capstone.feminacare.data.retrofit

import com.capstone.feminacare.data.response.BloodAnalysisResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("blood-analysis")
    suspend fun postMenstrualBlood(
        @Part file: MultipartBody.Part
    ) : BloodAnalysisResponse
}