package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "CINEMAS")
class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CINEMA_ID")
    var cinemaId: Long? = null

    @Column(name = "NAME", nullable = false)
    var name: String = ""

    @Column(name = "ADDRESS", nullable = false)
    var address: String = ""

    @Column(name = "CITY", nullable = false)
    var city: String = ""

    @Column(name = "PHONE")
    var phone: String? = null

    @Column(name = "STATUS", nullable = false)
    var status: String = ""

    @Column(name = "CREATED_AT", nullable = false)
    var createdAt: LocalDateTime? = null

    @Column(name = "UPDATED_AT", nullable = false)
    var updatedAt: LocalDateTime? = null
}