package com.aimovie.backend.controller

import com.aimovie.backend.entity.Cinema
import com.aimovie.backend.service.CinemaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cinemas")
class CinemaController(
    private val cinemaService: CinemaService
) {

    @GetMapping
    fun getAllCinemas(): ResponseEntity<List<Cinema>> =
        ResponseEntity.ok(cinemaService.findAll())

    @GetMapping("/{cinemaId}")
    fun getCinemaById(
        @PathVariable cinemaId: Long
    ): ResponseEntity<Cinema> {

        val cinema = cinemaService.findById(cinemaId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(cinema)
    }
}