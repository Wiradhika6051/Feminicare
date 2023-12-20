package com.capstone.feminacare.ui.auth.register

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.feminacare.data.Repository
import com.capstone.feminacare.data.remote.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    private val _successAlert = MutableLiveData<String>()
    val successAlert: LiveData<String> = _successAlert

    private val _errorAlert = MutableLiveData<String>()
    val errorAlert: LiveData<String> = _errorAlert

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(username: String, email: String, password: String, first_name: String, last_name: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val successresponse = repository.register(username, email, password, first_name, last_name)
                _isLoading.value = false
                _successAlert.value = successresponse.message!!

                Log.d(TAG, "isi message try ${successresponse.message}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                _errorAlert.value = errorResponse.message!!
                Log.d(TAG, "isi message catch ${errorResponse.message}")
                _isLoading.value = false
            } catch (e: Exception){
                _isLoading.value = false
                _errorAlert.value = "No Internet Connection"
            }
        }
    }
}