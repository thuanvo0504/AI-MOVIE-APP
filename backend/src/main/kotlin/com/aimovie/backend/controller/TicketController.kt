package com.aimovie.backend.controller

import com.aimovie.backend.entity.Ticket
import com.aimovie.backend.service.TicketService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tickets")
class TicketController(
    private val ticketService: TicketService
) {

    @GetMapping
    fun getAllTickets(
        authentication: Authentication
    ): ResponseEntity<List<Ticket>> {

        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalArgumentException(
                "Invalid authenticated user"
            )

        return ResponseEntity.ok(
            ticketService.findAllForUser(userId)
        )
    }

    @GetMapping("/{ticketId}")
    fun getTicketById(
        @PathVariable ticketId: Long,
        authentication: Authentication
    ): ResponseEntity<Ticket> {

        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalArgumentException(
                "Invalid authenticated user"
            )

        val ticket = ticketService.findByIdForUser(
            ticketId = ticketId,
            userId = userId
        ) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(ticket)
    }

    @GetMapping("/booking/{bookingId}")
    fun getTicketsByBookingId(
        @PathVariable bookingId: Long,
        authentication: Authentication
    ): ResponseEntity<List<Ticket>> {

        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalArgumentException(
                "Invalid authenticated user"
            )

        return ResponseEntity.ok(
            ticketService.findByBookingIdForUser(
                bookingId = bookingId,
                userId = userId
            )
        )
    }

    @GetMapping("/booking-seat/{bookingSeatId}")
    fun getTicketByBookingSeatId(
        @PathVariable bookingSeatId: Long,
        authentication: Authentication
    ): ResponseEntity<Ticket> {

        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalArgumentException(
                "Invalid authenticated user"
            )

        val ticket = ticketService.findByBookingSeatIdForUser(
            bookingSeatId = bookingSeatId,
            userId = userId
        ) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(ticket)
    }

    @GetMapping("/qr/{qrCode}")
    fun getTicketByQrCode(
        @PathVariable qrCode: String,
        authentication: Authentication
    ): ResponseEntity<Ticket> {

        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalArgumentException(
                "Invalid authenticated user"
            )

        val ticket = ticketService.findByQrCodeForUser(
            qrCode = qrCode,
            userId = userId
        ) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(ticket)
    }

    @PostMapping("/booking/{bookingId}")
    fun createTicketsForBooking(
        @PathVariable bookingId: Long,
        authentication: Authentication
    ): ResponseEntity<List<Ticket>> {

        val userId = authentication.name.toLongOrNull()
            ?: throw IllegalArgumentException(
                "Invalid authenticated user"
            )

        val tickets = ticketService.createTicketsForBooking(
            bookingId = bookingId,
            userId = userId
        )

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(tickets)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        exception: IllegalArgumentException
    ): ResponseEntity<Map<String, String>> {

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                mapOf(
                    "error" to (
                        exception.message
                            ?: "Invalid ticket request"
                        )
                )
            )
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(
        exception: IllegalStateException
    ): ResponseEntity<Map<String, String>> {

        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                mapOf(
                    "error" to (
                        exception.message
                            ?: "Ticket conflict"
                        )
                )
            )
    }
}