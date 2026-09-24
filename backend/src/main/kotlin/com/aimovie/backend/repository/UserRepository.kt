package com.aimovie.backend.repository

import com.aimovie.backend.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?
}