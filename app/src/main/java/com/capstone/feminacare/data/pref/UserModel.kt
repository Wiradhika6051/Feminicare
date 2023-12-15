package com.capstone.feminacare.data.pref

class UserModel(
    val username: String,
    val token: String,
    val isLogin: Boolean = false,
    val message: String? = null
)
