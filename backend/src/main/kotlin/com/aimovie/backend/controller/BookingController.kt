package com.aimovie.backend.controller

import com.aimovie.backend.dto.ConfirmBookingRequest
import com.aimovie.backend.entity.Booking
import com.aimovie.backend.service.BookingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
    // LẤY BOOKING THEO BOOKING CODE
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
    // API CŨ - TẠO BOOKING
    // ==========================================

    @PostMapping
    fun createBooking(
        @RequestBody booking: Booking
    ): ResponseEntity<Booking> {

        val savedBooking =
            bookingService.createBooking(
                booking
            )

        return ResponseEntity.ok(
            savedBooking
        )
    }


    // ==========================================
    // XÁC NHẬN BOOKING + THANH TOÁN
    // ==========================================

    @PostMapping("/confirm")
    fun confirmBooking(
        @RequestBody request: ConfirmBookingRequest
    ): ResponseEntity<Any> {

        println("========================================")
        println("CONTROLLER STEP 1 - ĐÃ VÀO /api/bookings/confirm")
        println("userId = ${request.userId}")
        println("showtimeId = ${request.showtimeId}")
        println("seats = ${request.showtimeSeatIds}")
        println("paymentMethod = ${request.paymentMethod}")
        println("========================================")


        return try {

            println(
                "CONTROLLER STEP 2 - GỌI bookingService.confirmBooking"
            )

            val result =
                bookingService.confirmBooking(
                    request
                )


            println(
                "CONTROLLER STEP 3 - SERVICE TRẢ KẾT QUẢ"
            )

            println(
                "bookingId = ${result.bookingId}"
            )

            println(
                "bookingCode = ${result.bookingCode}"
            )


            ResponseEntity.ok(
                result
            )

        } catch (
            e: IllegalArgumentException
        ) {

            println(
                "CONTROLLER ERROR - IllegalArgumentException"
            )

            e.printStackTrace()


            ResponseEntity
                .badRequest()
                .body(
                    mapOf(
                        "message" to
                            (
                                e.message
                                    ?: "Dữ liệu không hợp lệ"
                                )
                    )
                )

        } catch (
            e: IllegalStateException
        ) {

            println(
                "CONTROLLER ERROR - IllegalStateException"
            )

            e.printStackTrace()


            ResponseEntity
                .status(409)
                .body(
                    mapOf(
                        "message" to
                            (
                                e.message
                                    ?: "Ghế không còn khả dụng"
                                )
                    )
                )

        } catch (
            e: Exception
        ) {

            println(
                "CONTROLLER ERROR - Exception"
            )

            e.printStackTrace()


            ResponseEntity
                .internalServerError()
                .body(
                    mapOf(
                        "message" to
                            (
                                e.message
                                    ?: "Lỗi server"
                                )
                    )
                )
        }
    }
}