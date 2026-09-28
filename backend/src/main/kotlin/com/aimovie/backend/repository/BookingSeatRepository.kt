package com.aimovie.backend.repository

import com.aimovie.backend.entity.BookingSeat
import org.springframework.data.jpa.repository.JpaRepository

interface BookingSeatRepository : JpaRepository<BookingSeat, Long> {

    fun findByBookingId(bookingId: Long): List<BookingSeat>
}