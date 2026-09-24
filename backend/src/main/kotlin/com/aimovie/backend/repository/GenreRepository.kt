package com.aimovie.backend.repository

import com.aimovie.backend.entity.Genre
import org.springframework.data.jpa.repository.JpaRepository

interface GenreRepository : JpaRepository<Genre, Long>