package com.example.cinema.model

data class Movie(

    // =========================
    // THÔNG TIN CƠ BẢN
    // =========================

    val id: Int,

    val title: String,

    val poster: Int,

    val duration: Int,

    // ⭐ ĐIỂM ĐÁNH GIÁ PHIM
    val rating: Double,

    val genre: String,


    // =========================
    // PHÂN LOẠI KIỂM DUYỆT
    // =========================

    // Ví dụ: T13, T16, T18, K, P
    val ageRating: String,

    // Nội dung giải thích kiểm duyệt
    val certificationDescription: String = "",


    // =========================
    // NỘI DUNG
    // =========================

    val description: String,


    // =========================
    // THÔNG TIN CHI TIẾT
    // =========================

    val director: String = "",

    val actors: String = "",

    val language: String = "",


    // =========================
    // TRẠNG THÁI
    // =========================

    // NOW_SHOWING
    // UPCOMING
    // ENDED
    val status: String = "NOW_SHOWING",

    val releaseDate: String = ""
)