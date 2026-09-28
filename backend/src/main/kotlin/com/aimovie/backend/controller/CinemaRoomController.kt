package com.aimovie.backend.controller

import com.aimovie.backend.entity.CinemaRoom
import com.aimovie.backend.service.CinemaRoomService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cinema-rooms")
class CinemaRoomController(
    private val cinemaRoomService: CinemaRoomService
) {

    @GetMapping
    fun getAllCinemaRooms(): ResponseEntity<List<CinemaRoom>> =
        ResponseEntity.ok(cinemaRoomService.findAll())

    @GetMapping("/{roomId}")
    fun getCinemaRoomById(
        @PathVariable roomId: Long
    ): ResponseEntity<CinemaRoom> {

        val room = cinemaRoomService.findById(roomId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(room)
    }

    @GetMapping("/cinema/{cinemaId}")
    fun getCinemaRoomsByCinemaId(
        @PathVariable cinemaId: Long
    ): ResponseEntity<List<CinemaRoom>> {

        return ResponseEntity.ok(
            cinemaRoomService.findByCinemaId(cinemaId)
        )
    }
}