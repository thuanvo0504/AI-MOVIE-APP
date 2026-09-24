package com.aimovie.backend.repository

import com.aimovie.backend.entity.ShowtimeSeat
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ShowtimeSeatRepository : JpaRepository<ShowtimeSeat, Long> {

    fun findByShowtimeId(showtimeId: Long): List<ShowtimeSeat>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ShowtimeSeat s WHERE s.showtimeId = :showtimeId")
    fun findByShowtimeIdForUpdate(
        @Param("showtimeId") showtimeId: Long
    ): List<ShowtimeSeat>
}