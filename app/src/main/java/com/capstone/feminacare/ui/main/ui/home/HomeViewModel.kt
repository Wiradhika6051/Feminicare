package com.capstone.feminacare.ui.main.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.feminacare.data.Repository
import com.capstone.feminacare.utils.TimeUtils

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _timeOfDay = MutableLiveData<String>().apply {
        value = TimeUtils.getTimeOfDay()
    }

    val timeOfDay: LiveData<String> = _timeOfDay

    fun getArticles() = repository.getArticles("Woman Period Health")
    
}