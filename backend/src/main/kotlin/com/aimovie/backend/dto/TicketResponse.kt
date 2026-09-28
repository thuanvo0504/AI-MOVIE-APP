package com.aimovie.backend.dto

import java.math.BigDecimal

data class TicketResponse(
    val bookingId: Long,
    val bookingCode: String,
    val movieName: String,
    val posterUrl: String?,
    val cinemaName: String,
    val cinemaAddress: String,
    val roomName: String,
    val date: String,
    val showtime: String,
    val seats: String,
    val totalAmount: BigDecimal,
    val bookingStatus: String,
    val paymentMethod: String?,
    val paymentStatus: String?,
    val transactionCode: String?
)