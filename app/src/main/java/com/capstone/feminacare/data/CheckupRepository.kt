package com.capstone.feminacare.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.feminacare.data.local.BloodCheckup
import com.capstone.feminacare.data.local.BloodCheckupDao
import com.capstone.feminacare.data.local.CheckupDatabase
import com.capstone.feminacare.data.remote.response.BloodAnalysisResponse
import com.capstone.feminacare.data.remote.retrofit.ApiConfig
import okhttp3.MultipartBody
import retrofit2.HttpException

class CheckupRepository(
    private val checkupDao: BloodCheckupDao,
) {

    private val healthInfo = listOf(
        "Healthy",
        "UnHealthy"
    )

    fun postMenstrualBlood(
        photo: MultipartBody.Part,
        userCookies: String
    ): LiveData<Result<BloodAnalysisResponse>> = liveData {
        emit(Result.Loading)
        try {
            Log.d("BLOOD COOKIES", userCookies)
            val apiService = ApiConfig.getApiConfig(userCookies)
            val success = apiService.postMenstrualBlood(photo)
//            val colorIndex = ColorIndex.fromInt(success.data.colorIndex) as ColorIndex
//            val bloodIndex = BloodIndex(colorIndex, null)
//            val desc = bloodIndex.getDescription()
//
//            val healthInfo = if(bloodIndex.isHealthy == true) {
//                "Healty"
//            } else {
//                "Unhealthy"
//            }
//
//            println(bloodIndex)

//            val checkup = BloodCheckup(
//                timeStamp = System.currentTimeMillis(),
//                healthInfo = healthInfo,
//                description =
//            )
//            checkupDao.insertCheckup(checkup)
            emit(Result.Success(success))
        } catch (e: HttpException) {
            val errResponse = e.response()?.errorBody()?.string()
            emit(Result.Error(errResponse.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

//    fun getCheckupHistory(): Flow<PagingData<BloodCheckup>> {
//        val items = Pager(
//            config = PagingConfig(pageSize = 10, enablePlaceholders = false, initialLoadSize = 10)
//        ) {
//            BloodCheckupPagingSource(checkupDao)
//        }.flow
//        return items
//    }

    fun getCheckup(): LiveData<List<BloodCheckup>> {
        return checkupDao.getLiveCheckupHistory()
    }

    suspend fun insertBloodCheckup(checkup: BloodCheckup) {
        checkupDao.insertCheckup(checkup)
    }

    companion object {
        @Volatile
        private var instance: CheckupRepository? = null

        fun getInstance(context: Context): CheckupRepository {
            return instance ?: synchronized(this) {
                instance ?: CheckupRepository(
                    checkupDao = CheckupDatabase.getInstance(context).checkupDao(),
                ).also {
                    instance = it
                }
            }
        }
    }
}