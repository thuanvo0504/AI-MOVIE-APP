package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "SEAT_TYPES")
class SeatType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_TYPE_ID")
    var seatTypeId: Long? = null

    @Column(name = "NAME", nullable = false)
    var name: String = ""

    @Column(name = "DESCRIPTION")
    var description: String? = null
}