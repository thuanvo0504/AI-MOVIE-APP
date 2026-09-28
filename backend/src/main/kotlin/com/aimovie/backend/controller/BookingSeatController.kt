package com.aimovie.backend.controller

import com.aimovie.backend.entity.BookingSeat
import com.aimovie.backend.service.BookingSeatService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/booking-seats")
class BookingSeatController(
    private val bookingSeatService: BookingSeatService
) {

    @GetMapping
    fun getAllBookingSeats(): ResponseEntity<List<BookingSeat>> =
        ResponseEntity.ok(bookingSeatService.findAll())

    @GetMapping("/{bookingSeatId}")
    fun getBookingSeatById(
        @PathVariable bookingSeatId: Long
    ): ResponseEntity<BookingSeat> {

        val bookingSeat = bookingSeatService.findById(bookingSeatId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(bookingSeat)
    }

    @GetMapping("/booking/{bookingId}")
    fun getBookingSeatsByBookingId(
        @PathVariable bookingId: Long
    ): ResponseEntity<List<BookingSeat>> {

        return ResponseEntity.ok(
            bookingSeatService.findByBookingId(bookingId)
        )
    }
}