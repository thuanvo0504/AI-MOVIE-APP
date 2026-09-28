package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "MOVIES")
class Movie {

    @Id
    @Column(name = "MOVIE_ID")
    var movieId: Long? = null

    @Column(name = "TITLE", nullable = false)
    var title: String = ""

    @Lob
    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "POSTER_URL")
    var posterUrl: String? = null

    @Column(name = "TRAILER_URL")
    var trailerUrl: String? = null

    @Column(name = "DURATION_MINUTES", nullable = false)
    var durationMinutes: Int = 0

    @Column(name = "AGE_RATING")
    var ageRating: String? = null

    @Column(name = "RELEASE_DATE")
    var releaseDate: LocalDate? = null

    @Column(name = "STATUS", nullable = false)
    var status: String = ""

    @Column(name = "GENRE")
    var genre: String? = null

    @Column(name = "DIRECTOR")
    var director: String? = null

    @Column(name = "ACTORS")
    var actors: String? = null

    @Column(name = "LANGUAGE")
    var language: String? = null

    @Column(name = "CREATED_AT")
    var createdAt: LocalDateTime? = null

    @Column(name = "UPDATED_AT")
    var updatedAt: LocalDateTime? = null
}