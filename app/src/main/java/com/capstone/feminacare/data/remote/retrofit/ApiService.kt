package com.capstone.feminacare.data.remote.retrofit

import com.capstone.feminacare.data.remote.response.BloodAnalysisResponse
import com.capstone.feminacare.data.remote.response.LoginResponse
import com.capstone.feminacare.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("blood-analysis")
    suspend fun postMenstrualBlood(
//        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ) : BloodAnalysisResponse
}