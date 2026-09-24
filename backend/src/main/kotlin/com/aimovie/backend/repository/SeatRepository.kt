package com.aimovie.backend.repository

import com.aimovie.backend.entity.Seat
import org.springframework.data.jpa.repository.JpaRepository

interface SeatRepository : JpaRepository<Seat, Long>