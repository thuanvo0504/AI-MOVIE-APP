package com.aimovie.backend.repository

import com.aimovie.backend.entity.Cinema
import org.springframework.data.jpa.repository.JpaRepository

interface CinemaRepository : JpaRepository<Cinema, Long>