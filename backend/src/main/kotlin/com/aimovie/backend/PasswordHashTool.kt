package com.aimovie.backend

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

fun main() {

    val encoder = BCryptPasswordEncoder()

    val password = "Test@123"

    val hash = encoder.encode(password)

    println("==============================")
    println("PASSWORD: $password")
    println("BCrypt HASH:")
    println(hash)
    println("==============================")

    println(
        "Kiểm tra: ${
            encoder.matches(password, hash)
        }"
    )
}