package com.capstone.feminacare.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.feminacare.data.Repository
import com.capstone.feminacare.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository): ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}