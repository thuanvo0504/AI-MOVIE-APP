package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "CINEMA_ROOMS")
class CinemaRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROOM_ID")
    var roomId: Long? = null

    @Column(name = "CINEMA_ID", nullable = false)
    var cinemaId: Long = 0

   @Column(name = "NAME", nullable = false)
    var roomName: String = ""
    @Column(name = "CAPACITY", nullable = false)
    var capacity: Int = 0

    @Column(name = "STATUS", nullable = false)
    var status: String = ""

    @Column(name = "CREATED_AT", nullable = false)
    var createdAt: LocalDateTime? = null

    @Column(name = "UPDATED_AT", nullable = false)
    var updatedAt: LocalDateTime? = null
}