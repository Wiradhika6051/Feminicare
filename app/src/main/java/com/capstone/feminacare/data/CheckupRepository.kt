package com.capstone.feminacare.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.feminacare.data.local.BloodCheckup
import com.capstone.feminacare.data.local.BloodCheckupDao
import com.capstone.feminacare.data.local.CheckupDatabase
import com.capstone.feminacare.data.remote.response.BloodAnalysisResponse
import com.capstone.feminacare.data.remote.retrofit.ApiConfig
import com.capstone.feminacare.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import retrofit2.HttpException

class CheckupRepository(
    private val apiService: ApiService = ApiConfig.getApiConfig(),
    private val checkupDao: BloodCheckupDao
) {

    private val healthInfo = listOf(
        "Healthy",
        "UnHealthy"
    )

    fun postMenstrualBlood(
        photo: MultipartBody.Part
    ): LiveData<Result<BloodAnalysisResponse>> = liveData {
        emit(Result.Loading)
        try {
            val success = apiService.postMenstrualBlood(photo)
            val checkup = BloodCheckup(
                timeStamp = System.currentTimeMillis(),
                healthInfo = healthInfo.random(),
                description = success.message
            )
            checkupDao.insertCheckup(checkup)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val errResponse = e.response()?.errorBody()?.string()
            emit(Result.Error(errResponse.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getCheckupHistory(): List<BloodCheckup> = checkupDao.getCheckupHistory()

    suspend fun insertBloodCheckup(checkup: BloodCheckup) {
        checkupDao.insertCheckup(checkup)
    }

    companion object {
        @Volatile
        private var instance: CheckupRepository? = null

        fun getInstance(context: Context): CheckupRepository {
            return instance ?: synchronized(this) {
                instance ?: CheckupRepository(
                    checkupDao = CheckupDatabase.getInstance(context).checkupDao()
                ).also {
                    instance = it
                }
            }
        }
    }
}