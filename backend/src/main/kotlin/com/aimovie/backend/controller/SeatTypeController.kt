package com.aimovie.backend.controller

import com.aimovie.backend.entity.SeatType
import com.aimovie.backend.service.SeatTypeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/seat-types")
class SeatTypeController(
    private val seatTypeService: SeatTypeService
) {

    @GetMapping
    fun getAllSeatTypes(): ResponseEntity<List<SeatType>> {
        return ResponseEntity.ok(seatTypeService.findAll())
    }

    @GetMapping("/{seatTypeId}")
    fun getSeatTypeById(
        @PathVariable seatTypeId: Long
    ): ResponseEntity<SeatType> {

        val seatType = seatTypeService.findById(seatTypeId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(seatType)
    }
}