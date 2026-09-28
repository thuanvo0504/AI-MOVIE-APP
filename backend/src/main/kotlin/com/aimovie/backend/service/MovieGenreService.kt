package com.aimovie.backend.service

import com.aimovie.backend.entity.MovieGenre
import com.aimovie.backend.repository.MovieGenreRepository
import org.springframework.stereotype.Service

@Service
class MovieGenreService(
    private val movieGenreRepository: MovieGenreRepository
) {

    fun findAll(): List<MovieGenre> {
        return movieGenreRepository.findAll()
    }

    fun findByMovieId(movieId: Long): List<MovieGenre> {
        return movieGenreRepository.findByMovieId(movieId)
    }

    fun findByGenreId(genreId: Long): List<MovieGenre> {
        return movieGenreRepository.findByGenreId(genreId)
    }
}