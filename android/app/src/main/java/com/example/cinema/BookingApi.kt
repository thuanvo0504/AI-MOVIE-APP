package com.example.cinema

data class BookingApi(

    val bookingId: Long?,

    val userId: Long,

    val showtimeId: Long,

    val bookingCode: String,

    val totalAmount: Double,

    val status: String,

    val createdAt: String?,

    val updatedAt: String?
)