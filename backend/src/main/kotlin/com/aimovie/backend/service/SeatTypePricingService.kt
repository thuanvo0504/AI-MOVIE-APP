package com.aimovie.backend.service

import com.aimovie.backend.entity.SeatTypePricing
import com.aimovie.backend.repository.SeatTypePricingRepository
import org.springframework.stereotype.Service

@Service
class SeatTypePricingService(
    private val seatTypePricingRepository: SeatTypePricingRepository
) {

    fun findAll(): List<SeatTypePricing> {
        return seatTypePricingRepository.findAll()
    }

    fun findById(pricingId: Long): SeatTypePricing? {
        return seatTypePricingRepository.findById(pricingId).orElse(null)
    }

    fun findByShowtimeId(showtimeId: Long): List<SeatTypePricing> {
        return seatTypePricingRepository.findByShowtimeId(showtimeId)
    }
}