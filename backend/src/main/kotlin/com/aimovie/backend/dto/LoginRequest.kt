package com.aimovie.backend.dto

data class LoginRequest(
    val email: String,
    val password: String
)