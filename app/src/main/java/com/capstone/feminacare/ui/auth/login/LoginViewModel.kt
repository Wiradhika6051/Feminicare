package com.capstone.feminacare.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.capstone.feminacare.data.Repository
import com.capstone.feminacare.data.pref.UserModel
import com.capstone.feminacare.data.remote.response.LoginResponse
import kotlinx.coroutines.launch

//class LoginViewModel(private val repository: Repository) : ViewModel() {
//    private val _account= MutableLiveData<Result<LoginResponse>>()
//    val account : LiveData<Result<LoginResponse>> = _account
//    fun login(username: String, password: String){
//        viewModelScope.launch {
//            repository.loginAccount(username, password).asFlow().collect{
//                _account.value = it
//            }
//        }
//    }
//    fun setupAction(userModel: UserModel){
//        viewModelScope.launch {
//            repository.saveSession(userModel)
//        }
//    }
//}