package com.aimovie.backend.service

import com.aimovie.backend.dto.UserResponse
import com.aimovie.backend.entity.User
import com.aimovie.backend.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun login(email: String, password: String): User? {
        val user = userRepository.findByEmail(email) ?: return null

        if (!passwordEncoder.matches(password, user.passwordHash)) {
            return null
        }

        return user
    }

    fun findAll(): List<UserResponse> {
        return userRepository.findAll().map { user ->
            UserResponse(
                userId = user.userId!!,
                fullName = user.fullName,
                email = user.email,
                phone = user.phone,
                role = user.role,
                status = user.status
            )
        }
    }
}