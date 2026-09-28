package com.aimovie.backend.dto

data class BookingRequest(
    val showtimeId: Long,
    val showtimeSeatIds: List<Long>
)