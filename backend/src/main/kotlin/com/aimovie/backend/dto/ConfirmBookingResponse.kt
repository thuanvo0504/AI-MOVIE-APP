package com.aimovie.backend.dto

import java.math.BigDecimal

data class ConfirmBookingResponse(
    val bookingId: Long,
    val bookingCode: String,
    val paymentId: Long,
    val transactionCode: String,
    val totalAmount: BigDecimal,
    val bookingStatus: String,
    val paymentStatus: String
)