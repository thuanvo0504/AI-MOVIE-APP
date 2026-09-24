package com.aimovie.backend.dto

data class SeatAvailabilityResponse(

    val showtimeSeatId: Long,

    val showtimeId: Long,

    val seatId: Long,

    val rowName: String,

    val seatNumber: Int,

    val seatTypeId: Long,

    val status: String,

    val price: Double
)