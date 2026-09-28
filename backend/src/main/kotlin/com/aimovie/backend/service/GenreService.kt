package com.aimovie.backend.service

import com.aimovie.backend.entity.Genre
import com.aimovie.backend.repository.GenreRepository
import org.springframework.stereotype.Service

@Service
class GenreService(
    private val genreRepository: GenreRepository
) {

    fun findAll(): List<Genre> {
        return genreRepository.findAll()
    }

    fun findById(genreId: Long): Genre? {
        return genreRepository.findById(genreId).orElse(null)
    }
}