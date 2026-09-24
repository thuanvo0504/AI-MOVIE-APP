package com.aimovie.backend.service

import com.aimovie.backend.entity.Seat
import com.aimovie.backend.repository.SeatRepository
import org.springframework.stereotype.Service

@Service
class SeatService(
    private val seatRepository: SeatRepository
) {

    fun findAll(): List<Seat> {
        return seatRepository.findAll()
    }

    fun findById(seatId: Long): Seat? {
        return seatRepository.findById(seatId).orElse(null)
    }
}