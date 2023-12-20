package com.capstone.feminacare.ui.main

import androidx.lifecycle.ViewModel
import com.capstone.feminacare.data.PredictionRepository
import com.capstone.feminacare.data.remote.response.MenstrualCycleResponse

class MainViewModel(private val repository: PredictionRepository): ViewModel() {
    fun getPrediction() : List<MenstrualCycleResponse> = repository.getPredictions()
}