package com.aimovie.backend.service

import com.aimovie.backend.entity.Ticket
import com.aimovie.backend.repository.BookingRepository
import com.aimovie.backend.repository.BookingSeatRepository
import com.aimovie.backend.repository.PaymentRepository
import com.aimovie.backend.repository.TicketRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class TicketService(
    private val ticketRepository: TicketRepository,
    private val bookingRepository: BookingRepository,
    private val bookingSeatRepository: BookingSeatRepository,
    private val paymentRepository: PaymentRepository
) {

    // ============================================================
    // DATA-LEVEL AUTHORIZATION
    // ============================================================

    fun findAllForUser(userId: Long): List<Ticket> {
        return ticketRepository.findAll()
            .filter { ticket ->
                val booking = bookingRepository
                    .findById(ticket.bookingId)
                    .orElse(null)

                booking?.userId == userId
            }
    }

    fun findByIdForUser(
        ticketId: Long,
        userId: Long
    ): Ticket? {

        val ticket = ticketRepository.findById(ticketId)
            .orElse(null)
            ?: return null

        val booking = bookingRepository
            .findById(ticket.bookingId)
            .orElse(null)
            ?: return null

        return if (booking.userId == userId) {
            ticket
        } else {
            null
        }
    }

    fun findByBookingIdForUser(
        bookingId: Long,
        userId: Long
    ): List<Ticket> {

        val booking = bookingRepository
            .findById(bookingId)
            .orElse(null)
            ?: return emptyList()

        if (booking.userId != userId) {
            return emptyList()
        }

        return ticketRepository.findByBookingId(bookingId)
    }

    fun findByBookingSeatIdForUser(
        bookingSeatId: Long,
        userId: Long
    ): Ticket? {

        val bookingSeat = bookingSeatRepository
            .findById(bookingSeatId)
            .orElse(null)
            ?: return null

        val booking = bookingRepository
            .findById(bookingSeat.bookingId)
            .orElse(null)
            ?: return null

        if (booking.userId != userId) {
            return null
        }

        return ticketRepository.findByBookingSeatId(bookingSeatId)
    }

    fun findByQrCodeForUser(
        qrCode: String,
        userId: Long
    ): Ticket? {

        val ticket = ticketRepository
            .findByQrCode(qrCode)
            ?: return null

        val booking = bookingRepository
            .findById(ticket.bookingId)
            .orElse(null)
            ?: return null

        return if (booking.userId == userId) {
            ticket
        } else {
            null
        }
    }

    // ============================================================
    // CREATE TICKETS
    // ============================================================

    @Transactional
    fun createTicketsForBooking(
        bookingId: Long,
        userId: Long
    ): List<Ticket> {

        val booking = bookingRepository.findById(bookingId)
            .orElseThrow {
                IllegalArgumentException(
                    "Booking not found: $bookingId"
                )
            }

        // DATA-LEVEL AUTHORIZATION
        if (booking.userId != userId) {
            throw IllegalStateException(
                "You are not allowed to access this booking"
            )
        }

        if (booking.status != "CONFIRMED") {
            throw IllegalStateException(
                "Booking is not confirmed"
            )
        }

        val hasSuccessfulPayment = paymentRepository
            .findByBookingId(bookingId)
            .any { it.status == "SUCCESS" }

        if (!hasSuccessfulPayment) {
            throw IllegalStateException(
                "Booking has not been paid successfully"
            )
        }

        val bookingSeats = bookingSeatRepository
            .findByBookingId(bookingId)

        if (bookingSeats.isEmpty()) {
            throw IllegalArgumentException(
                "No booking seats found for booking: $bookingId"
            )
        }

        val now = LocalDateTime.now()

        return bookingSeats.map { bookingSeat ->

            val bookingSeatId = bookingSeat.bookingSeatId!!

            val existingTicket =
                ticketRepository.findByBookingSeatId(bookingSeatId)

            existingTicket ?: run {

                val ticket = Ticket().apply {
                    this.bookingId = bookingId
                    this.bookingSeatId = bookingSeatId
                    this.qrCode =
                        "QR-${bookingId}-${bookingSeatId}-${UUID.randomUUID()}"
                    this.status = "VALID"
                    this.createdAt = now
                }

                ticketRepository.save(ticket)
            }
        }
    }
}