package com.capstone.feminacare.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.feminacare.data.PredictionRepository
import com.capstone.feminacare.data.pref.UserModel
import com.capstone.feminacare.data.remote.response.MenstrualCycleResponse
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.feminacare.data.Repository
import com.capstone.feminacare.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: PredictionRepository): ViewModel() {
    fun getPrediction() : List<MenstrualCycleResponse> = repository.getPredictions()
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}