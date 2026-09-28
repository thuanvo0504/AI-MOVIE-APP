package com.example.cinema

data class LoginResponse(
    val userId: Long,
    val fullName: String,
    val email: String,
    val role: String,
    val token: String
)