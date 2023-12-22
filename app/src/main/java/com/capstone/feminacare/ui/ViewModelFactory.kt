package com.capstone.feminacare.ui

//import com.capstone.feminacare.ui.auth.login.LoginViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.feminacare.data.Repository
import com.capstone.feminacare.di.Injection
import com.capstone.feminacare.ui.auth.login.LoginViewModel
import com.capstone.feminacare.ui.auth.register.RegisterViewModel
import com.capstone.feminacare.ui.chatbot.ChatBotViewModel
import com.capstone.feminacare.ui.main.MainViewModel
import com.capstone.feminacare.ui.main.ui.notifications.NotificationsViewModel

class ViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ChatBotViewModel::class.java) -> {
                ChatBotViewModel(repository) as T
            }

            modelClass.isAssignableFrom(NotificationsViewModel::class.java) -> {
                NotificationsViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }



            else -> throw IllegalArgumentException("Unknown View Model : ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                )
            }
        }
    }
}