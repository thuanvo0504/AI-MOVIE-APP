package com.aimovie.backend.repository

import com.aimovie.backend.entity.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long> {

    fun findByMovieId(movieId: Long): List<Review>

    fun findByUserId(userId: Long): List<Review>

    fun findByUserIdAndMovieId(userId: Long, movieId: Long): Review?
}