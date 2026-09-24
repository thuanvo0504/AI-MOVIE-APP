package com.aimovie.backend.controller

import com.aimovie.backend.entity.Movie
import com.aimovie.backend.service.MovieService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movies")
class MovieController(
    private val movieService: MovieService
) {

    @GetMapping
    fun getAllMovies(): ResponseEntity<List<Movie>> {
        return ResponseEntity.ok(movieService.findAll())
    }

    @GetMapping("/{movieId}")
    fun getMovieById(
        @PathVariable movieId: Long
    ): ResponseEntity<Movie> {
        val movie = movieService.findById(movieId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(movie)
    }
}