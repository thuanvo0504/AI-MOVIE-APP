package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "USERS")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    var userId: Long? = null,

    @Column(name = "NAME", nullable = false)
    var fullName: String,

    @Column(name = "EMAIL", nullable = false, unique = true)
    var email: String,

    @Column(name = "PASSWORD_HASH", nullable = false)
    var passwordHash: String,

    @Column(name = "PHONE")
    var phone: String? = null,

    @Column(name = "ROLE", nullable = false)
    var role: String,

    @Column(name = "STATUS", nullable = false)
    var status: String,

    @Column(name = "CREATED_AT", nullable = false)
    var createdAt: LocalDateTime? = null,

    @Column(name = "UPDATED_AT", nullable = false)
    var updatedAt: LocalDateTime? = null
)