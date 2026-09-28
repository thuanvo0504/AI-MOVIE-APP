package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "SHOWTIME_SEATS")
class ShowtimeSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHOWTIME_SEAT_ID")
    var showtimeSeatId: Long? = null

    @Column(name = "SHOWTIME_ID", nullable = false)
    var showtimeId: Long = 0

    @Column(name = "SEAT_ID", nullable = false)
    var seatId: Long = 0

    @Column(name = "STATUS", nullable = false)
    var status: String = ""

    @Column(name = "HELD_AT")
    var heldAt: LocalDateTime? = null

    @Column(name = "BOOKED_AT")
    var bookedAt: LocalDateTime? = null
}