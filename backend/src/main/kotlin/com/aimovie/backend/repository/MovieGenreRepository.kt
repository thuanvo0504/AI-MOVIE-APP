package com.aimovie.backend.repository

import com.aimovie.backend.entity.MovieGenre
import com.aimovie.backend.entity.MovieGenreId
import org.springframework.data.jpa.repository.JpaRepository

interface MovieGenreRepository : JpaRepository<MovieGenre, MovieGenreId> {

    fun findByMovieId(movieId: Long): List<MovieGenre>

    fun findByGenreId(genreId: Long): List<MovieGenre>
}