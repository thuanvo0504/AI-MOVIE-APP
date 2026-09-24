package com.aimovie.backend.repository

import com.aimovie.backend.entity.Ticket
import org.springframework.data.jpa.repository.JpaRepository

interface TicketRepository : JpaRepository<Ticket, Long> {

    fun findByBookingId(bookingId: Long): List<Ticket>

    fun findByBookingSeatId(bookingSeatId: Long): Ticket?

    fun findByQrCode(qrCode: String): Ticket?
}