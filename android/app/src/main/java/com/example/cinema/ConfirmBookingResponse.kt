package com.example.cinema

data class ConfirmBookingResponse(
    val bookingId: Long,
    val bookingCode: String,
    val paymentId: Long,
    val transactionCode: String,
    val totalAmount: Double,
    val bookingStatus: String,
    val paymentStatus: String
)