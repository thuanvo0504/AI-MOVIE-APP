package com.aimovie.backend

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class BackendApplication {

    @Bean
    fun testDatabase(
        jdbcTemplate: JdbcTemplate
    ): CommandLineRunner {

        return CommandLineRunner {

            try {

                val user =
                    jdbcTemplate.queryForObject(
                        "SELECT USER FROM DUAL",
                        String::class.java
                    )

                println("====================================")
                println("DATABASE CONNECTION SUCCESS!")
                println("Connected user: $user")
                println("====================================")

            } catch (e: Exception) {

                println("====================================")
                println("DATABASE CONNECTION FAILED!")
                println(e.message)
                println("====================================")
            }
        }
    }
}

fun main(args: Array<String>) {

    runApplication<BackendApplication>(*args)
}