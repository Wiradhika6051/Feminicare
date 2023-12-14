package com.capstone.feminacare.data


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.capstone.feminacare.data.remote.response.ArticleResponse
import com.capstone.feminacare.data.remote.response.BotMessage
import com.capstone.feminacare.data.remote.response.Message
import com.capstone.feminacare.data.remote.response.UserMessage
import com.capstone.feminacare.data.remote.retrofit.ApiConfig
import com.capstone.feminacare.data.remote.retrofit.ApiService
import kotlinx.coroutines.delay
import retrofit2.HttpException

class Repository(private val apiService: ApiService = ApiConfig.getApiConfig()) {


    private val _messages = MutableLiveData<Result<List<Message>>>()
    val messages: LiveData<Result<List<Message>>> get() = _messages

    suspend fun sendChatbotMessage(message: String) {
        try {
            val userMessage = UserMessage(message, System.currentTimeMillis())
            val updatedMessage =
                (_messages.value as? Result.Success)?.data.orEmpty().plus(userMessage)
            _messages.postValue(Result.Success(updatedMessage))

            delay(2000L)

            val chatBot = femiBotChatbot(message)

            val botMessage = BotMessage(chatBot, System.currentTimeMillis())
            val updatedMessageBotResponse = updatedMessage.plus(botMessage)
            _messages.postValue(Result.Success(updatedMessageBotResponse))
        } catch (e: Exception) {
            _messages.postValue(Result.Error("An error occured"))
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