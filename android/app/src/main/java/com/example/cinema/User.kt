package com.example.cinema

data class User(
    val userId: Long,
    val fullName: String,
    val email: String,
    val phone: String,
    val role: String,
    val status: String
)