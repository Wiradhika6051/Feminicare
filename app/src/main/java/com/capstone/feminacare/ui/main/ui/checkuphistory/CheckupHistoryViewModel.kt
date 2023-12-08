package com.capstone.feminacare.ui.main.ui.checkuphistory

import androidx.lifecycle.ViewModel
import com.capstone.feminacare.data.CheckupRepository

class CheckupHistoryViewModel(repository: CheckupRepository) : ViewModel() {

    val checkupHistory = repository.getCheckupHistory()
}