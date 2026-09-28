package com.aimovie.backend

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    // =====================================================
    // PASSWORD ENCODER
    // =====================================================

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


    // =====================================================
    // JWT ROLE CONVERTER
    // =====================================================

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {

        val grantedAuthoritiesConverter =
            JwtGrantedAuthoritiesConverter()

        grantedAuthoritiesConverter.setAuthoritiesClaimName(
            "role"
        )

        grantedAuthoritiesConverter.setAuthorityPrefix(
            "ROLE_"
        )

        return JwtAuthenticationConverter().apply {

            setJwtGrantedAuthoritiesConverter(
                grantedAuthoritiesConverter
            )
        }
    }


    // =====================================================
    // SECURITY FILTER
    // =====================================================

    @Bean
    fun securityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain {

        http

            // REST API -> tắt CSRF
            .csrf {
                it.disable()
            }

            // JWT -> không dùng session
            .sessionManagement {

                it.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }


            // =================================================
            // PHÂN QUYỀN API
            // =================================================

            .authorizeHttpRequests { auth ->

                auth

                    // =========================================
                    // LOGIN
                    // =========================================

                    .requestMatchers(
                        "/api/auth/login"
                    )
                    .permitAll()


                    // =========================================
                    // API CÔNG KHAI
                    // =========================================

                    .requestMatchers(
                        "/api/movies/**",
                        "/api/showtimes/**",
                        "/api/showtime-seats/**",
                        "/api/seats/**",
                        "/api/seat-types/**",
                        "/api/seat-type-pricing/**",
                        "/api/cinemas/**",
                        "/api/cinema-rooms/**",
                        "/api/genres/**",
                        "/api/movie-genres/**"
                    )
                    .permitAll()


                    // =========================================
                    // QUẢN LÝ USER
                    // Chỉ ADMIN
                    // =========================================

                    .requestMatchers(
                        "/api/users/**"
                    )
                    .hasRole(
                        "ADMIN"
                    )


                    // =========================================
                    // BOOKING
                    // USER + ADMIN
                    // =========================================

                    .requestMatchers(
                        "/api/bookings/**"
                    )
                    .hasAnyRole(
                        "USER",
                        "ADMIN"
                    )


                    // =========================================
                    // PAYMENT
                    // USER + ADMIN
                    // =========================================

                    .requestMatchers(
                        "/api/payments/**"
                    )
                    .hasAnyRole(
                        "USER",
                        "ADMIN"
                    )


                    // =========================================
                    // BOOKING SEATS
                    // USER + ADMIN
                    // =========================================

                    .requestMatchers(
                        "/api/booking-seats/**"
                    )
                    .hasAnyRole(
                        "USER",
                        "ADMIN"
                    )


                    // =========================================
                    // API KHÁC
                    // Chỉ cần đăng nhập
                    // =========================================

                    .anyRequest()
                    .authenticated()
            }


            // =================================================
            // JWT RESOURCE SERVER
            // =================================================

            .oauth2ResourceServer { oauth2 ->

                oauth2.jwt { jwt ->

                    jwt.jwtAuthenticationConverter(
                        jwtAuthenticationConverter()
                    )
                }
            }


        return http.build()
    }
}