package com.example.cinema

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val userId: Long?
)