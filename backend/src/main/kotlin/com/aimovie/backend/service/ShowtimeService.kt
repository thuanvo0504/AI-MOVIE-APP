package com.aimovie.backend.service

import com.aimovie.backend.entity.Showtime
import com.aimovie.backend.repository.ShowtimeRepository
import org.springframework.stereotype.Service

@Service
class ShowtimeService(
    private val showtimeRepository: ShowtimeRepository
) {

    fun findAll(): List<Showtime> {
        return showtimeRepository.findAll()
    }

    fun findById(showtimeId: Long): Showtime? {
        return showtimeRepository.findById(showtimeId).orElse(null)
    }
}