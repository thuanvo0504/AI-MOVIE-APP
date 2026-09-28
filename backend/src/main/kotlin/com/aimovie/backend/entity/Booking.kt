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
@Table(name = "BOOKINGS")
class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKING_ID")
    var bookingId: Long? = null

    @Column(name = "USER_ID", nullable = false)
    var userId: Long = 0

    @Column(name = "SHOWTIME_ID", nullable = false)
    var showtimeId: Long = 0

    @Column(name = "BOOKING_CODE", nullable = false)
    var bookingCode: String = ""

    @Column(name = "TOTAL_AMOUNT", nullable = false)
    var totalAmount: BigDecimal = BigDecimal.ZERO

    @Column(name = "STATUS", nullable = false)
    var status: String = ""

    @Column(name = "CREATED_AT")
    var createdAt: LocalDateTime? = null

    @Column(name = "UPDATED_AT")
    var updatedAt: LocalDateTime? = null
}