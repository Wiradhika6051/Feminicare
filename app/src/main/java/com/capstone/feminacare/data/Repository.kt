package com.capstone.feminacare.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.capstone.feminacare.data.remote.response.ArticleResponse
import com.capstone.feminacare.data.remote.response.BloodAnalysisResponse
import com.capstone.feminacare.data.remote.response.BotMessage
import com.capstone.feminacare.data.remote.response.Message
import com.capstone.feminacare.data.remote.response.UserMessage
import com.capstone.feminacare.data.remote.retrofit.ApiConfig
import com.capstone.feminacare.data.remote.retrofit.ApiService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import retrofit2.HttpException

class Repository(private val apiService: ApiService = ApiConfig.getApiConfig()) {
    fun postMenstrualBlood(
        photo: MultipartBody.Part
    ): LiveData<Result<BloodAnalysisResponse>> = liveData {
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

    private val _messages = MutableLiveData<Result<List<Message>>>()
    val messages: LiveData<Result<List<Message>>> get() = _messages

    suspend fun sendChatbotMessage(message: String) {

        coroutineScope {
            try {
                val userMessage = UserMessage(message, System.currentTimeMillis())
                val updatedMessage = (_messages.value as? Result.Success)?.data.orEmpty().plus(userMessage)
                _messages.postValue(Result.Success(updatedMessage))

                delay(2000L)

                val botMessage = BotMessage("Result for $message", System.currentTimeMillis())
                val updatedMessageBotResponse = updatedMessage.plus(botMessage)
                _messages.postValue(Result.Success(updatedMessageBotResponse))
            } catch (e: Exception) {
                _messages.postValue(Result.Error("An error occured"))
            }
        }

    }

    fun getArticles(query: String): LiveData<Result<ArticleResponse>> = liveData {
        emit(Result.Loading)
        try {
            val apiService = ApiConfig.getRapidApiConfig()
            val success = apiService.getArticles(query = query)
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