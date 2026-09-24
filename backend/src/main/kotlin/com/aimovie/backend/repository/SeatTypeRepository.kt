package com.aimovie.backend.repository

import com.aimovie.backend.entity.SeatType
import org.springframework.data.jpa.repository.JpaRepository

interface SeatTypeRepository : JpaRepository<SeatType, Long>