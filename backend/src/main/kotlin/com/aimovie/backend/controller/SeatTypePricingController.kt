package com.aimovie.backend.controller

import com.aimovie.backend.entity.SeatTypePricing
import com.aimovie.backend.service.SeatTypePricingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/seat-type-pricing")
class SeatTypePricingController(
    private val seatTypePricingService: SeatTypePricingService
) {

    @GetMapping("/showtime/{showtimeId}")
    fun getPricingByShowtime(
        @PathVariable showtimeId: Long
    ): ResponseEntity<List<SeatTypePricing>> {

        return ResponseEntity.ok(
            seatTypePricingService.findByShowtimeId(showtimeId)
        )
    }

    @GetMapping("/{pricingId}")
    fun getPricingById(
        @PathVariable pricingId: Long
    ): ResponseEntity<SeatTypePricing> {

        val pricing = seatTypePricingService.findById(pricingId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(pricing)
    }
}