package com.capstone.feminacare.di

import android.content.Context
import com.capstone.feminacare.data.CheckupRepository
import com.capstone.feminacare.data.Repository

object Injection {
    fun provideRepository(): Repository {
        return Repository.getInstance()
    }

    fun provideCheckupRepository(context: Context): CheckupRepository {
        return CheckupRepository.getInstance(context)
    }
}