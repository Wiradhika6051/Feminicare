package com.capstone.feminacare.ui.bloodcheckup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.feminacare.data.CheckupRepository
import com.capstone.feminacare.data.local.BloodCheckup
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class BloodCheckupViewModel(private val repository: CheckupRepository) : ViewModel() {

    fun postPhoto(
        file: MultipartBody.Part,
        userCookies: String
    ) = repository.postMenstrualBlood(file, userCookies)

    fun insertBloodCheckup(checkup: BloodCheckup) {
        viewModelScope.launch {
            repository.insertBloodCheckup(checkup)
        }
    }
}