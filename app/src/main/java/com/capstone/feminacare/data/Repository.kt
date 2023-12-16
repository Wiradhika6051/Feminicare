package com.capstone.feminacare.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.capstone.feminacare.data.pref.UserModel
import com.capstone.feminacare.data.pref.UserPreference
import com.capstone.feminacare.data.remote.response.ArticleResponse
import com.capstone.feminacare.data.remote.response.BloodAnalysisResponse
import com.capstone.feminacare.data.remote.response.BotMessage
import com.capstone.feminacare.data.remote.response.LoginResponse
import com.capstone.feminacare.data.remote.response.Message
import com.capstone.feminacare.data.remote.response.RegisterResponse
import com.capstone.feminacare.data.remote.response.UserMessage
import com.capstone.feminacare.data.remote.retrofit.ApiConfig
import com.capstone.feminacare.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.net.SocketTimeoutException

class Repository(private val userPreference: UserPreference, private val apiService: ApiService = ApiConfig.getApiConfig()) {
    fun login(username: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.login(username, password)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Result.Error(errorResponse.message ?: "Unknown error"))
        } catch (e: SocketTimeoutException) {
            emit(Result.Error("Request timeout. Please check your internet connection."))
        }
    }

    suspend fun register(username: String, email: String, password: String, first_name: String, last_name: String): RegisterResponse {
        // Panggil metode register dari ApiService di sini
        return apiService.register(username, email, password, first_name, last_name)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }
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

        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
    }
}