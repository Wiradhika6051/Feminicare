package com.capstone.feminacare.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.feminacare.data.response.BloodAnalysisResponse
import com.capstone.feminacare.data.retrofit.ApiConfig
import com.capstone.feminacare.data.retrofit.ApiService
import okhttp3.MultipartBody
import retrofit2.HttpException

class Repository (private val apiService: ApiService = ApiConfig.getApiConfig()) {
    fun postMenstrualBlood(
        photo: MultipartBody.Part
    ) : LiveData<Result<BloodAnalysisResponse>> = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.postMenstrualBlood(photo)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val errResponse = e.response()?.errorBody()?.string()
            emit(Result.Error(errResponse.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(): Repository {
            return instance ?: synchronized(this) {
                instance ?: Repository().also {
                    instance = it
                }
            }
        }
    }
}