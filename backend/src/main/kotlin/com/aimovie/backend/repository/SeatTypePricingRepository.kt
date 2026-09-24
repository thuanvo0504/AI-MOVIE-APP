package com.aimovie.backend.repository

import com.aimovie.backend.entity.SeatTypePricing
import org.springframework.data.jpa.repository.JpaRepository

interface SeatTypePricingRepository : JpaRepository<SeatTypePricing, Long> {

    fun findByShowtimeId(showtimeId: Long): List<SeatTypePricing>
}