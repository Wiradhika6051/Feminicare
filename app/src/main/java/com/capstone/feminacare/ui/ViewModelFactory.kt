package com.capstone.feminacare.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.feminacare.data.Repository
import com.capstone.feminacare.di.Injection
import com.capstone.feminacare.ui.chatbot.ChatBotViewModel
import com.capstone.feminacare.ui.main.MainViewModel

class ViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ChatBotViewModel::class.java) -> {
                ChatBotViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown View Model : ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(),
                )
            }
        }
    }
}