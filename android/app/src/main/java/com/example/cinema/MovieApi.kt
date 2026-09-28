package com.example.cinema

data class MovieApi(

    val movieId: Long?,

    val title: String,

    val description: String?,

    val durationMinutes: Int,

    val releaseDate: String?,

    val posterUrl: String?,

    val trailerUrl: String?,

    val status: String,

    val ageRating: String?,

    val genre: String?,

    val director: String?,

    val actors: String?,

    val language: String?,

    val createdAt: String?,

    val updatedAt: String?
)