package com.aimovie.backend.repository

import com.aimovie.backend.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<Payment, Long> {

    fun findByBookingId(bookingId: Long): List<Payment>

    fun findByTransactionCode(transactionCode: String): Payment?
}