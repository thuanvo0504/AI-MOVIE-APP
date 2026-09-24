package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "SHOWTIMES")
class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHOWTIME_ID")
    var showtimeId: Long? = null

    @Column(name = "MOVIE_ID", nullable = false)
    var movieId: Long = 0

    @Column(name = "ROOM_ID", nullable = false)
    var roomId: Long = 0

    @Column(name = "START_TIME", nullable = false)
    var startTime: LocalDateTime = LocalDateTime.MIN

    @Column(name = "END_TIME", nullable = false)
    var endTime: LocalDateTime = LocalDateTime.MIN

    @Column(name = "BASE_PRICE", nullable = false)
    var basePrice: BigDecimal = BigDecimal.ZERO

    @Column(name = "STATUS", nullable = false)
    var status: String = ""

    @Column(name = "CREATED_AT")
    var createdAt: LocalDateTime? = null

    @Column(name = "UPDATED_AT")
    var updatedAt: LocalDateTime? = null
}