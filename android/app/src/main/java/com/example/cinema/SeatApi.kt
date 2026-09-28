package com.example.cinema

data class SeatApi(

    val showtimeSeatId: Long,

    val showtimeId: Long,

    val seatId: Long,

    val rowName: String,

    val seatNumber: Int,

    val seatTypeId: Long,

    val status: String,

    val price: Double
)