package com.aimovie.backend.service

import com.aimovie.backend.entity.Cinema
import com.aimovie.backend.repository.CinemaRepository
import org.springframework.stereotype.Service

@Service
class CinemaService(
    private val cinemaRepository: CinemaRepository
) {

    fun findAll(): List<Cinema> {
        return cinemaRepository.findAll()
    }

    fun findById(cinemaId: Long): Cinema? {
        return cinemaRepository.findById(cinemaId).orElse(null)
    }
}