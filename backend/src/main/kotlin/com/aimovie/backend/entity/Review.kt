package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "REVIEWS")
class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    var reviewId: Long? = null

    @Column(name = "USER_ID", nullable = false)
    var userId: Long = 0

    @Column(name = "MOVIE_ID", nullable = false)
    var movieId: Long = 0

    @Column(name = "RATING", nullable = false)
    var rating: Int = 0

    @Column(name = "COMMENT_TEXT")
var comment: String? = null

    @Column(name = "STATUS", nullable = false)
    var status: String = ""

    @Column(name = "CREATED_AT", nullable = false)
var createdAt: LocalDateTime? = null

@Column(name = "UPDATED_AT", nullable = false)
var updatedAt: LocalDateTime? = null
}