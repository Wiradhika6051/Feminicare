package com.capstone.feminacare.di

import android.content.Context
import com.capstone.feminacare.data.CheckupRepository
import com.capstone.feminacare.data.PredictionRepository
import com.capstone.feminacare.data.Repository
import com.capstone.feminacare.data.pref.UserPreference
import com.capstone.feminacare.data.pref.dataStore
import com.capstone.feminacare.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiConfig()
        return Repository.getInstance(pref, apiService)
    }

    fun provideCheckupRepository(context: Context): CheckupRepository {
        return CheckupRepository.getInstance(context)
    }

    fun providePredictionRepository(context: Context) : PredictionRepository {
        return PredictionRepository.getInstance(context)
    }
}