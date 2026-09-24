package com.aimovie.backend.controller

import com.aimovie.backend.dto.UserResponse
import com.aimovie.backend.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun getAllUsers(): List<UserResponse> {
        return userService.findAll()
    }

    @GetMapping("/email")
    fun getUserByEmail(
        @RequestParam email: String
    ): UserResponse? {
        return userService.findByEmail(email)?.let { user ->
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