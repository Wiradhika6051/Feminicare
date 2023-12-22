package com.capstone.feminacare.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.feminacare.data.Repository
import com.capstone.feminacare.data.pref.UserModel

class MainViewModel(
    private val mainRepository: Repository
) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return mainRepository.getSession().asLiveData()
    }
}