package com.aimovie.backend

import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class JwtService(
    private val jwtEncoder: JwtEncoder
) {

    fun generateToken(
        userId: Long,
        email: String,
        role: String
    ): String {

        val now = Instant.now()

        val claims = JwtClaimsSet.builder()
            .issuer("ai-movie-backend")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(3600))
            .subject(userId.toString())
            .claim("email", email)
            .claim("role", role)
            .build()

        return jwtEncoder
            .encode(JwtEncoderParameters.from(claims))
            .tokenValue
    }
}