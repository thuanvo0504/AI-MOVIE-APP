package com.example.cinema

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val phone: String? = null
)