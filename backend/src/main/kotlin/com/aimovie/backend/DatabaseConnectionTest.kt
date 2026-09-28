package com.aimovie.backend

import org.springframework.boot.CommandLineRunner
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class DatabaseConnectionTest(
    private val jdbcTemplate: JdbcTemplate
) : CommandLineRunner {

    override fun run(vararg args: String) {
        val result = jdbcTemplate.queryForObject(
            "SELECT USER FROM DUAL",
            String::class.java
        )

        println("====================================")
        println("DATABASE CONNECTION SUCCESS!")
        println("Connected user: $result")
        println("====================================")
    }
}