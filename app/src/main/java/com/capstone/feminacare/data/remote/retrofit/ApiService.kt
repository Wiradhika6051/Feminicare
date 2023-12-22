package com.capstone.feminacare.data.remote.retrofit

import com.capstone.feminacare.data.remote.response.BloodAnalysisResponse
import com.capstone.feminacare.data.remote.response.ChatBotV2Response
import com.capstone.feminacare.data.remote.response.GetUserProfileResponse
import com.capstone.feminacare.data.remote.response.LoginResponse
import com.capstone.feminacare.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("profile/{id}")
    suspend fun getUserProfile(
        @Path("id") userId: String,
    ): GetUserProfileResponse

    @Multipart
    @POST("blood-analysis")
    suspend fun postMenstrualBlood(
//        @Header("Authorization") cookies: String,
        @Part file: MultipartBody.Part
    ) : BloodAnalysisResponse

    @FormUrlEncoded
    @POST("chatbot")
    suspend fun getChatbot(
        @Field("prompt") prompt: String
    ) : ChatBotV2Response
}