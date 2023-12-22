package com.capstone.feminacare.data


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.capstone.feminacare.data.pref.UserModel
import com.capstone.feminacare.data.pref.UserPreference
import com.capstone.feminacare.data.remote.response.ArticleResponse
import com.capstone.feminacare.data.remote.response.BotMessage
import com.capstone.feminacare.data.remote.response.LoginResponse
import com.capstone.feminacare.data.remote.response.Message
import com.capstone.feminacare.data.remote.response.RegisterResponse
import com.capstone.feminacare.data.remote.response.UserMessage
import com.capstone.feminacare.data.remote.retrofit.ApiConfig
import com.capstone.feminacare.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

class Repository(
    private val userPreference: UserPreference,
    private val apiService: ApiService = ApiConfig.getApiConfig()
) {

    fun login(username: String, password: String) = liveData {
        emit(Result.Loading)
        try {
//            val successResponse = apiService.login(username, password)
            val loginCall: Call<LoginResponse> = apiService.login(username, password)
            var data: LoginResponse? = null

            // Enqueue the call to run it asynchronously
            loginCall.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        // Access headers, including Set-Cookie
                        val setCookieHeader = response.headers()["Set-Cookie"]
                        val semicolonIndex = setCookieHeader?.indexOf(';')


// Extract the substring before the semicolon
                        val extractedCookie = if (semicolonIndex != -1 && semicolonIndex != null) {
                            setCookieHeader.substring(0, semicolonIndex)
                        } else {
                            setCookieHeader
                        }

                        val equalsIndex = extractedCookie?.indexOf('=')

// Extract the substring after '='
                        val result = if (equalsIndex != -1) {
                            extractedCookie?.substring(equalsIndex?.plus(1) ?: 0)
                        } else {
                            extractedCookie
                        }

                        Log.d("Set-Cookie header: BEFORE", extractedCookie.toString())
                        Log.d("Set-Cookie header:", result ?: "none")
                        val userId = response.body()?.loginData?.userId
                        // Use withContext to switch to the coroutine context
                        CoroutineScope(Dispatchers.Default).launch {
                            val login = UserModel(username, result ?: "", true, userId ?: "")
                            Log.d("LOGIN DATA", login.userId)
                            saveSession(login)
                            // Emit the success result within the coroutine context
                            data = response.body()!!
                        }
                    } else {
                        // Handle unsuccessful response
                        // Use withContext to switch to the coroutine context
                        CoroutineScope(Dispatchers.Default).launch {
                            throw Exception(response.message().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Handle failure
                    // Use withContext to switch to the coroutine context
                    CoroutineScope(Dispatchers.Default).launch {
                        emit(Result.Error(t.message ?: "Unknown Error"))
                    }
                }
            })
            emit(Result.Success(data))
        } catch (e: HttpException) {
//            val errorBody = e.response()?.errorBody()?.string()
//            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Result.Error(e.message ?: "Unknown error"))
        } catch (e: SocketTimeoutException) {
            emit(Result.Error("Request timeout. Please check your internet connection."))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown Err"))
        }
    }

    suspend fun register(
        username: String,
        email: String,
        password: String,
        first_name: String,
        last_name: String
    ): RegisterResponse {
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

    private val _messages = MutableLiveData<Result<List<Message>>>()
    val messages: LiveData<Result<List<Message>>> get() = _messages

    suspend fun sendChatbotMessage(message: String, cookies: String) {
        try {
            val userMessage = UserMessage(message, System.currentTimeMillis())
            val updatedMessage =
                (_messages.value as? Result.Success)?.data.orEmpty().plus(userMessage)
            _messages.postValue(Result.Success(updatedMessage))

//            delay(2000L)
            val authApi = ApiConfig.getApiConfig(cookies)
            val response = authApi.getChatbot(message)
            Log.d("bot response", response.data?.response.toString())

//            val chatBot = femiBotChatbot(message)
            val messageResponse = response.data?.response


            val botMessage = BotMessage(
                messageResponse ?: "Maaf, aku tidak mengerti pertanyaan kamu.",
                System.currentTimeMillis()
            )
            val updatedMessageBotResponse = updatedMessage.plus(botMessage)
            _messages.postValue(Result.Success(updatedMessageBotResponse))
        } catch (e: Exception) {
            _messages.postValue(Result.Error("An error occured ${e.message}"))
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

    private fun femiBotChatbot(userInput: String): String {
        val haloPattern = Regex("(?i)\\b(halo)\\b")
        val siapaPattern = Regex("(?i)\\b(kamu siapa)\\b")
        val olahragaPattern = Regex("(?i)\\b(olahraga aman dilakukan saat menstruasi)\\b")
        val terimakasihPattern = Regex("(?i)\\b(terimakasih)\\b")

        return when {
            haloPattern.containsMatchIn(userInput) -> "Hai! femibot di sini. Mau tau informasi tentang apa nih?"
            siapaPattern.containsMatchIn(userInput) -> "Aku adalah teman bicara kamu untuk membahas permasalahan menstruasi. Jangan malu untuk bertanya dengan aku tentang permasalahan kamu."
            olahragaPattern.containsMatchIn(userInput) ->
                "Olahraga aman dilakukan saat menstruasi, dan bahkan bisa membantu meredakan beberapa gejala seperti kram perut. Olahraga ringan seperti berjalan, berenang, atau yoga bisa bermanfaat. Namun, dengarkan tubuh Anda dan hindari aktivitas yang terlalu intens jika Anda merasa tidak nyaman. Selalu jaga kebersihan diri saat berolahraga selama menstruasi dengan mengganti produk menstruasi sesuai kebutuhan."

            terimakasihPattern.containsMatchIn(userInput) -> "Dengan senang hati :)"
            else -> "Maaf, aku tidak mengerti pertanyaan kamu."
        }
    }

    fun getUserProfile(cookies: String, userId: String) = liveData {
        emit(Result.Loading)
        try {
            val authApi = ApiConfig.getApiConfig(cookies)
            val successResponse = authApi.getUserProfile(userId)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Result.Error(errorResponse.message ?: "Unknown error"))
        } catch (e: SocketTimeoutException) {
            emit(Result.Error("Request timeout. Please check your internet connection."))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): Repository {
            return instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService).also {
                    instance = it
                }
            }
        }
    }
}
