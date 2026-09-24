package com.aimovie.backend.repository

import com.aimovie.backend.entity.Showtime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ShowtimeRepository :
    JpaRepository<Showtime, Long> {

    @Query(
        value = """
            SELECT *
            FROM SHOWTIMES
            WHERE MOVIE_ID = :movieId
              AND STATUS = 'ACTIVE'
              AND START_TIME >= LOCALTIMESTAMP
            ORDER BY START_TIME
        """,
        nativeQuery = true
    )
    fun findAvailableByMovieId(
        @Param("movieId")
        movieId: Long
    ): List<Showtime>
}