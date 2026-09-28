package com.aimovie.backend.repository

import com.aimovie.backend.entity.Movie
import org.springframework.data.jpa.repository.JpaRepository

interface MovieRepository : JpaRepository<Movie, Long>