package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "TICKETS")
class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_ID")
    var ticketId: Long? = null

    @Column(name = "BOOKING_ID", nullable = false)
    var bookingId: Long = 0

    @Column(name = "BOOKING_SEAT_ID", nullable = false)
    var bookingSeatId: Long = 0

    @Column(name = "QR_CODE", nullable = false)
    var qrCode: String = ""

    @Column(name = "STATUS", nullable = false)
    var status: String = ""


    @Column(name = "CREATED_AT", nullable = false)
var createdAt: LocalDateTime? = null
}