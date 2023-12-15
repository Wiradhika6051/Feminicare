package com.capstone.feminacare.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.capstone.feminacare.data.pref.UserModel
import com.capstone.feminacare.data.pref.UserPreference
import com.capstone.feminacare.data.remote.response.ArticleResponse
import com.capstone.feminacare.data.remote.response.BloodAnalysisResponse
import com.capstone.feminacare.data.remote.response.BotMessage
import com.capstone.feminacare.data.remote.response.Message
import com.capstone.feminacare.data.remote.response.RegisterResponse
import com.capstone.feminacare.data.remote.response.UserMessage
import com.capstone.feminacare.data.remote.retrofit.ApiConfig
import com.capstone.feminacare.data.remote.retrofit.ApiService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.HttpException

class Repository(private val userPreference: UserPreference, private val apiService: ApiService = ApiConfig.getApiConfig()) {
    suspend fun loginAccount(username: String, password: String) = liveData{
        emit(Result.Loading)
        try {
            val response = apiService.login(username, password)
            emit(Result.Success(response))
        }catch (e:Exception){
            emit(Result.Error(e.message.toString()))
        }
        // Implementasi logika login di sini, gantilah dengan logika sesuai kebutuhan Anda

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