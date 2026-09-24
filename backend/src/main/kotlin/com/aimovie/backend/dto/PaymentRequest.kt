package com.aimovie.backend.dto

import java.math.BigDecimal

data class PaymentRequest(
    val bookingId: Long,
    val amount: BigDecimal,
    val paymentMethod: String
)