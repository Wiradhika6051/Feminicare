package com.capstone.feminacare.data.pref

class UserModel(
    val username: String,
    val cookies: String,
    val isLogin: Boolean = false,
    val userId: String,
    val message: String? = null
)
