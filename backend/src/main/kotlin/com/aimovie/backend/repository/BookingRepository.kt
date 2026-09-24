package com.aimovie.backend.repository

import com.aimovie.backend.entity.Booking
import org.springframework.data.jpa.repository.JpaRepository

interface BookingRepository : JpaRepository<Booking, Long> {

    fun findByBookingCode(bookingCode: String): Booking?

    fun findByUserId(userId: Long): List<Booking>
}