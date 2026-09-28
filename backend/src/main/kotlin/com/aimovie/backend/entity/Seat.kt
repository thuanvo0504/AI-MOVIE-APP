package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "SEATS")
class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    var seatId: Long? = null

    @Column(name = "ROOM_ID", nullable = false)
    var roomId: Long = 0

    @Column(name = "SEAT_TYPE_ID", nullable = false)
    var seatTypeId: Long = 0

    @Column(name = "ROW_NAME", nullable = false)
    var rowLabel: String = ""

    @Column(name = "SEAT_NUMBER", nullable = false)
    var seatNumber: Long = 0

    @Column(name = "STATUS", nullable = false)
    var status: String = ""
}