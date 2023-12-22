package com.capstone.feminacare.ui.main.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.capstone.feminacare.data.Repository
import com.capstone.feminacare.data.Result
import com.capstone.feminacare.data.remote.response.GetUserProfileResponse
import kotlinx.coroutines.launch

class NotificationsViewModel(private val repository: Repository) : ViewModel() {

    private val _profileResult = MutableLiveData<Result<GetUserProfileResponse>>()
    val profileResult: LiveData<Result<GetUserProfileResponse>> = _profileResult

    fun getUserProfile(cookies: String, userId: String) {
        viewModelScope.launch {
            repository.getUserProfile(cookies, userId).asFlow().collect{
                _profileResult.value = it
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
