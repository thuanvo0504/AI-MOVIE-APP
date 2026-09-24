package com.aimovie.backend.service

import com.aimovie.backend.entity.SeatType
import com.aimovie.backend.repository.SeatTypeRepository
import org.springframework.stereotype.Service

@Service
class SeatTypeService(
    private val seatTypeRepository: SeatTypeRepository
) {

    fun findAll(): List<SeatType> {
        return seatTypeRepository.findAll()
    }

    fun findById(seatTypeId: Long): SeatType? {
        return seatTypeRepository.findById(seatTypeId).orElse(null)
    }
}