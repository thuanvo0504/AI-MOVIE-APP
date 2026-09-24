package com.aimovie.backend.dto

data class UserResponse(
    val userId: Long,
    val fullName: String,
    val email: String,
    val phone: String?,
    val role: String,
    val status: String
)