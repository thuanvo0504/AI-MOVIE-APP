package com.aimovie.backend.controller

import com.aimovie.backend.entity.Review
import com.aimovie.backend.service.ReviewService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reviews")
class ReviewController(
    private val reviewService: ReviewService
) {

    @GetMapping
    fun getAllReviews(): ResponseEntity<List<Review>> =
        ResponseEntity.ok(reviewService.findAll())

    @GetMapping("/{reviewId}")
    fun getReviewById(
        @PathVariable reviewId: Long
    ): ResponseEntity<Review> {

        val review = reviewService.findById(reviewId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(review)
    }

    @GetMapping("/movie/{movieId}")
    fun getReviewsByMovieId(
        @PathVariable movieId: Long
    ): ResponseEntity<List<Review>> {

        return ResponseEntity.ok(
            reviewService.findByMovieId(movieId)
        )
    }

    @GetMapping("/user/{userId}")
    fun getReviewsByUserId(
        @PathVariable userId: Long
    ): ResponseEntity<List<Review>> {

        return ResponseEntity.ok(
            reviewService.findByUserId(userId)
        )
    }

    @GetMapping("/user/{userId}/movie/{movieId}")
    fun getReviewByUserAndMovie(
        @PathVariable userId: Long,
        @PathVariable movieId: Long
    ): ResponseEntity<Review> {

        val review = reviewService.findByUserIdAndMovieId(
            userId,
            movieId
        ) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(review)
    }
}