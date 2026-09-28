package com.aimovie.backend.dto

data class ConfirmBookingRequest(
    val userId: Long,
    val showtimeId: Long,
    val showtimeSeatIds: List<Long>,
    val paymentMethod: String
)