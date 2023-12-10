package com.capstone.feminacare.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.feminacare.data.Repository
import com.capstone.feminacare.di.Injection
import com.capstone.feminacare.ui.main.ui.home.HomeViewModel

class MainViewModelFactory (
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown View Model : ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: MainViewModelFactory? = null

        @JvmStatic
        fun getInstance(): MainViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: MainViewModelFactory(
                    Injection.provideRepository(),
                )
            }
        }
    }
}