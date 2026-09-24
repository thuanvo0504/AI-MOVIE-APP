package com.aimovie.backend.repository

import com.aimovie.backend.entity.CinemaRoom
import org.springframework.data.jpa.repository.JpaRepository

interface CinemaRoomRepository : JpaRepository<CinemaRoom, Long> {

    fun findByCinemaId(cinemaId: Long): List<CinemaRoom>
}