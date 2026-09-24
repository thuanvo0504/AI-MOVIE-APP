package com.aimovie.backend.controller

import com.aimovie.backend.entity.Seat
import com.aimovie.backend.service.SeatService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/seats")
class SeatController(
    private val seatService: SeatService
) {

    @GetMapping
    fun getAllSeats(): ResponseEntity<List<Seat>> {
        return ResponseEntity.ok(seatService.findAll())
    }

    @GetMapping("/{seatId}")
    fun getSeatById(
        @PathVariable seatId: Long
    ): ResponseEntity<Seat> {

        val seat = seatService.findById(seatId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(seat)
    }
}