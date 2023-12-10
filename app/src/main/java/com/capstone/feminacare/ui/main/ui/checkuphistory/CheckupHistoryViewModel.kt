package com.capstone.feminacare.ui.main.ui.checkuphistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.feminacare.data.CheckupRepository
import com.capstone.feminacare.data.local.BloodCheckup

class CheckupHistoryViewModel(repository: CheckupRepository) : ViewModel() {

//    val checkupHistory: Flow<PagingData<BloodCheckup>> =
//        repository.getCheckupHistory().cachedIn(viewModelScope)

    val checkupHistory : LiveData<List<BloodCheckup>> = repository.getCheckup()
}