package com.bangkit.sortify.pref

data class User(
    val id: Int,
    val username: String,
    val token: String,
    val isLogin: Boolean = false
)