package com.aimovie.backend.service

import com.aimovie.backend.entity.CinemaRoom
import com.aimovie.backend.repository.CinemaRoomRepository
import org.springframework.stereotype.Service

@Service
class CinemaRoomService(
    private val cinemaRoomRepository: CinemaRoomRepository
) {

    fun findAll(): List<CinemaRoom> {
        return cinemaRoomRepository.findAll()
    }

    fun findById(roomId: Long): CinemaRoom? {
        return cinemaRoomRepository.findById(roomId).orElse(null)
    }

    fun findByCinemaId(cinemaId: Long): List<CinemaRoom> {
        return cinemaRoomRepository.findByCinemaId(cinemaId)
    }
}