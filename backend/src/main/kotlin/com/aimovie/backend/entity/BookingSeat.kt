package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "BOOKING_SEATS")
class BookingSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKING_SEAT_ID")
    var bookingSeatId: Long? = null

    @Column(name = "BOOKING_ID", nullable = false)
    var bookingId: Long = 0

    @Column(name = "SHOWTIME_SEAT_ID", nullable = false)
    var showtimeSeatId: Long = 0

    @Column(name = "PRICE", nullable = false)
    var price: BigDecimal = BigDecimal.ZERO
}