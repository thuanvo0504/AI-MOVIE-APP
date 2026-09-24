package com.aimovie.backend.service

import com.aimovie.backend.entity.Review
import com.aimovie.backend.repository.ReviewRepository
import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository
) {

    fun findAll(): List<Review> {
        return reviewRepository.findAll()
    }

    fun findById(reviewId: Long): Review? {
        return reviewRepository.findById(reviewId).orElse(null)
    }

    fun findByMovieId(movieId: Long): List<Review> {
        return reviewRepository.findByMovieId(movieId)
    }

    fun findByUserId(userId: Long): List<Review> {
        return reviewRepository.findByUserId(userId)
    }

    fun findByUserIdAndMovieId(
        userId: Long,
        movieId: Long
    ): Review? {
        return reviewRepository.findByUserIdAndMovieId(
            userId,
            movieId
        )
    }
}