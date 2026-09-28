package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "SEAT_TYPE_PRICING")
class SeatTypePricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_TYPE_PRICING_ID")
    var pricingId: Long? = null

    @Column(name = "SHOWTIME_ID", nullable = false)
    var showtimeId: Long = 0

    @Column(name = "SEAT_TYPE_ID", nullable = false)
    var seatTypeId: Long = 0

    @Column(name = "PRICE", nullable = false)
    var price: BigDecimal = BigDecimal.ZERO
}