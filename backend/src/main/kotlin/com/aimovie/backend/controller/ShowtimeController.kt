package com.aimovie.backend.controller

import com.aimovie.backend.entity.Showtime
import com.aimovie.backend.repository.ShowtimeRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/showtimes")
class ShowtimeController(
    private val showtimeRepository: ShowtimeRepository
) {

    @GetMapping("/movie/{movieId}")
    fun getShowtimesByMovie(
        @PathVariable movieId: Long
    ): ResponseEntity<List<Showtime>> {

        val showtimes =
            showtimeRepository.findAvailableByMovieId(
                movieId
            )

        return ResponseEntity.ok(
            showtimes
        )
    }
}