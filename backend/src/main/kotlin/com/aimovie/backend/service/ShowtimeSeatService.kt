package com.aimovie.backend.service

import com.aimovie.backend.entity.ShowtimeSeat
import com.aimovie.backend.repository.ShowtimeSeatRepository
import org.springframework.stereotype.Service

@Service
class ShowtimeSeatService(
    private val showtimeSeatRepository: ShowtimeSeatRepository
) {

    fun findByShowtimeId(showtimeId: Long): List<ShowtimeSeat> {
        return showtimeSeatRepository.findByShowtimeId(showtimeId)
    }

    fun findById(showtimeSeatId: Long): ShowtimeSeat? {
        return showtimeSeatRepository.findById(showtimeSeatId).orElse(null)
    }
}