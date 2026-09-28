package com.aimovie.backend.dto

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val userId: Long? = null
)