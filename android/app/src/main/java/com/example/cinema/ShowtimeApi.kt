package com.example.cinema

data class ShowtimeApi(

    val showtimeId: Long,

    val movieId: Long,

    val roomId: Long,

    val startTime: String,

    val endTime: String,

    val basePrice: Double,

    val status: String
)