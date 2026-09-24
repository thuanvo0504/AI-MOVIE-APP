package com.aimovie.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table

@Entity
@Table(name = "MOVIE_GENRES")
@IdClass(MovieGenreId::class)
class MovieGenre {

    @Id
    @Column(name = "MOVIE_ID", nullable = false)
    var movieId: Long = 0

    @Id
    @Column(name = "GENRE_ID", nullable = false)
    var genreId: Long = 0
}

data class MovieGenreId(
    var movieId: Long = 0,
    var genreId: Long = 0
) : java.io.Serializable