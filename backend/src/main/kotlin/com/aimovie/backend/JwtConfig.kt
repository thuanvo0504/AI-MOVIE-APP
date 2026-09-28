package com.aimovie.backend

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Configuration
class JwtConfig(
    @Value("\${app.jwt.secret}")
    private val secret: String
) {

    private val secretKey: SecretKey =
        SecretKeySpec(
            secret.toByteArray(StandardCharsets.UTF_8),
            "HmacSHA256"
        )

    @Bean
    fun jwtEncoder(): JwtEncoder {
        return NimbusJwtEncoder
            .withSecretKey(secretKey)
            .build()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder
            .withSecretKey(secretKey)
            .build()
    }
}