package com.aimovie.backend.controller

import com.aimovie.backend.entity.Genre
import com.aimovie.backend.service.GenreService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/genres")
class GenreController(
    private val genreService: GenreService
) {

    @GetMapping
    fun getAllGenres(): ResponseEntity<List<Genre>> =
        ResponseEntity.ok(genreService.findAll())

    @GetMapping("/{genreId}")
    fun getGenreById(
        @PathVariable genreId: Long
    ): ResponseEntity<Genre> {

        val genre = genreService.findById(genreId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(genre)
    }
}