package com.aimovie.backend.service

import com.aimovie.backend.entity.Movie
import com.aimovie.backend.repository.MovieRepository
import org.springframework.stereotype.Service

@Service
class MovieService(
    private val movieRepository: MovieRepository
) {

    fun findAll(): List<Movie> {
        return movieRepository.findAll()
    }

    fun findById(movieId: Long): Movie? {
        return movieRepository.findById(movieId).orElse(null)
    }
}