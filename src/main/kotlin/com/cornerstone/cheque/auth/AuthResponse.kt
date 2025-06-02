package com.cornerstone.cheque.auth

import com.cornerstone.cheque.model.User

data class AuthResponse(
    val token: String,
    val user: User
)