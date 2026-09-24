package com.aimovie.backend.controller

import com.aimovie.backend.entity.MovieGenre
import com.aimovie.backend.service.MovieGenreService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movie-genres")
class MovieGenreController(
    private val movieGenreService: MovieGenreService
) {

    @GetMapping
    fun getAllMovieGenres(): ResponseEntity<List<MovieGenre>> =
        ResponseEntity.ok(movieGenreService.findAll())

    @GetMapping("/movie/{movieId}")
    fun getGenresByMovieId(
        @PathVariable movieId: Long
    ): ResponseEntity<List<MovieGenre>> {

        return ResponseEntity.ok(
            movieGenreService.findByMovieId(movieId)
        )
    }

    @GetMapping("/genre/{genreId}")
    fun getMoviesByGenreId(
        @PathVariable genreId: Long
    ): ResponseEntity<List<MovieGenre>> {

        return ResponseEntity.ok(
            movieGenreService.findByGenreId(genreId)
        )
    }
}