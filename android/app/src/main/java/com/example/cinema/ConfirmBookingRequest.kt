package com.example.cinema

data class ConfirmBookingRequest(
    val userId: Long,
    val showtimeId: Long,
    val showtimeSeatIds: List<Long>,
    val paymentMethod: String
)