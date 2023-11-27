package com.capstone.feminacare.di

import com.capstone.feminacare.data.Repository

object Injection {
    fun provideRepository(): Repository {
        return Repository.getInstance()
    }
}