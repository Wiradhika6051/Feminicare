package com.capstone.feminacare.ui.bloodcheckup

import androidx.lifecycle.ViewModel
import com.capstone.feminacare.data.CheckupRepository
import okhttp3.MultipartBody

class BloodCheckupViewModel(private val repository: CheckupRepository) : ViewModel() {

    fun postPhoto(
        file: MultipartBody.Part
    ) = repository.postMenstrualBlood(file)

}