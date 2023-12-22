package com.capstone.feminacare.data.remote.retrofit

import android.util.Log
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getApiConfig(userCookies: String? = null): ApiService {
        if (userCookies != null) {
            Log.d("API COOKIES", userCookies)
            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "Bearer $userCookies")
                    .build()
                chain.proceed(requestHeaders)
            }
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://feminacare-406402.et.r.appspot.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)

        } else {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor).build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://feminacare-406402.et.r.appspot.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }


    fun performLoginAndGetCookie(username: String, password: String): String? {
        val formBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url("https://feminacare-406402.et.r.appspot.com/api/v1/auth/login")
            .post(formBody)
            .build()

        return try {
            val response = OkHttpClient().newCall(request).execute()
            Log.d("PERFORM LOGIN", response.toString())
            // Check if the response has the Set-Cookie header
            val setCookieHeader: String? = response.headers["Set-Cookie"]

            // Now, setCookieHeader contains the value of the Set-Cookie header
            setCookieHeader
        } catch (e: Exception) {
            // Handle the exception
            null
        }
    }

    fun getRapidApiConfig(): RapidApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("X-RapidAPI-Key", "7d5ab6e0e0msh15c8fa38821e9cdp1467f1jsn2148d3699ed2")
                .addHeader("X-RapidAPI-Host", "news67.p.rapidapi.com")
                .build()
            chain.proceed(requestHeaders)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://news67.p.rapidapi.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(RapidApiService::class.java)
    }
}