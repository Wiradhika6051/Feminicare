package com.capstone.feminacare.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.feminacare.data.CheckupRepository
import com.capstone.feminacare.di.Injection
import com.capstone.feminacare.ui.bloodcheckup.BloodCheckupViewModel

class CheckupViewModelFactory(private val repository: CheckupRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BloodCheckupViewModel::class.java) -> {
                BloodCheckupViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown View Model : ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: CheckupViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): CheckupViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: CheckupViewModelFactory(
                    Injection.provideCheckupRepository(context),
                )
            }
        }
    }
}