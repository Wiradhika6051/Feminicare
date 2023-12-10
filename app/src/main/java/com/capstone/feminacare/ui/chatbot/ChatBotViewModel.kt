package com.capstone.feminacare.ui.chatbot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.feminacare.data.Repository
import com.capstone.feminacare.data.Result
import com.capstone.feminacare.data.remote.response.Message
import kotlinx.coroutines.launch

class ChatBotViewModel(private val repository: Repository) : ViewModel() {
    val messages : LiveData<Result<List<Message>>> = repository.messages

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun sendChatbotMessage(message: String) {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                repository.sendChatbotMessage(message)
            } finally {
                _loading.postValue(false)
            }
        }
    }

}