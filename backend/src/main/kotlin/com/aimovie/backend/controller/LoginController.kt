package com.aimovie.backend.controller

import com.aimovie.backend.JwtService
import com.aimovie.backend.dto.LoginRequest
import com.aimovie.backend.dto.LoginResponse
import com.aimovie.backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class LoginController(
    private val userService: UserService,
    private val jwtService: JwtService
) {

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): ResponseEntity<LoginResponse> {

        val user = userService.login(
            request.email,
            request.password
        ) ?: return ResponseEntity.status(401).build()

        val token = jwtService.generateToken(
            userId = user.userId!!,
            email = user.email,
            role = user.role
        )

        return ResponseEntity.ok(
            LoginResponse(
                userId = user.userId!!,
                fullName = user.fullName,
                email = user.email,
                role = user.role,
                token = token
            )
        )
    }
}