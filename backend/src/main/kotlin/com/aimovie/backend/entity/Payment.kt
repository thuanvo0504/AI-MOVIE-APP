package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "PAYMENTS")
class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    var paymentId: Long? = null

    @Column(name = "BOOKING_ID", nullable = false)
    var bookingId: Long = 0

    @Column(name = "AMOUNT", nullable = false)
    var amount: BigDecimal = BigDecimal.ZERO

    @Column(name = "PAYMENT_METHOD", nullable = false)
    var paymentMethod: String = ""

    @Column(name = "STATUS", nullable = false)
    var status: String = ""

    @Column(name = "TRANSACTION_CODE")
    var transactionCode: String? = null


    @Column(name = "CREATED_AT")
    var createdAt: LocalDateTime? = null
}