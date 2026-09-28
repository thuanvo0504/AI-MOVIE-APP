package com.aimovie.backend.controller

import com.aimovie.backend.dto.ConfirmBookingRequest
import com.aimovie.backend.dto.ConfirmBookingResponse
import com.aimovie.backend.dto.TicketResponse
import com.aimovie.backend.entity.Booking
import com.aimovie.backend.service.BookingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingService: BookingService
) {

    // ==========================================
    // LẤY TẤT CẢ BOOKING
    // ==========================================

    @GetMapping
    fun getAllBookings(): ResponseEntity<List<Booking>> {

        return ResponseEntity.ok(
            bookingService.findAll()
        )
    }


    // ==========================================
    // LẤY BOOKING CƠ BẢN CỦA USER
    // ==========================================

    @GetMapping("/user/{userId}")
    fun getBookingsByUser(
        @PathVariable userId: Long
    ): ResponseEntity<List<Booking>> {

        return ResponseEntity.ok(
            bookingService.findByUserId(
                userId
            )
        )
    }


    // ==========================================
    // MY TICKETS - THÔNG TIN VÉ ĐẦY ĐỦ
    // ==========================================

    @GetMapping("/user/{userId}/tickets")
    fun getTicketsByUser(
        @PathVariable userId: Long
    ): ResponseEntity<List<TicketResponse>> {

        return ResponseEntity.ok(
            bookingService.findTicketsByUserId(
                userId
            )
        )
    }


    // ==========================================
    // LẤY BOOKING THEO CODE
    // ==========================================

    @GetMapping("/code/{bookingCode}")
    fun getBookingByCode(
        @PathVariable bookingCode: String
    ): ResponseEntity<Booking> {

        val booking =
            bookingService.findByBookingCode(
                bookingCode
            )
                ?: return ResponseEntity
                    .notFound()
                    .build()

        return ResponseEntity.ok(
            booking
        )
    }


    // ==========================================
    // LẤY BOOKING THEO ID
    // ==========================================

    @GetMapping("/{bookingId}")
    fun getBookingById(
        @PathVariable bookingId: Long
    ): ResponseEntity<Booking> {

        val booking =
            bookingService.findById(
                bookingId
            )
                ?: return ResponseEntity
                    .notFound()
                    .build()

        return ResponseEntity.ok(
            booking
        )
    }


    // ==========================================
    // API CŨ - TẠO BOOKING
    // ==========================================

    @PostMapping
    fun createBooking(
        @RequestBody booking: Booking
    ): ResponseEntity<Booking> {

        return ResponseEntity.ok(
            bookingService.createBooking(
                booking
            )
        )
    }


    // ==========================================
    // XÁC NHẬN BOOKING + THANH TOÁN
    // ==========================================

    @PostMapping("/confirm")
    fun confirmBooking(
        @RequestBody request: ConfirmBookingRequest
    ): ResponseEntity<ConfirmBookingResponse> {

        val result =
            bookingService.confirmBooking(
                request
            )

        return ResponseEntity.ok(
            result
        )
    }
}