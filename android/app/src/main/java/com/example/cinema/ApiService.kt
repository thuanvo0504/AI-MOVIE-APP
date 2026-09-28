package com.example.cinema

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // ==========================
    // ĐĂNG NHẬP
    // ==========================

    @POST("api/auth/login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>


    // ==========================
    // DANH SÁCH USER
    // ==========================

    @GET("api/users")
    fun getUsers(
        @Header("Authorization") authorization: String
    ): Call<List<User>>


    // ==========================
    // DANH SÁCH PHIM
    // ==========================

    @GET("api/movies")
    fun getMovies(
        @Header("Authorization") authorization: String
    ): Call<List<MovieApi>>


    // ==========================
    // CHI TIẾT PHIM
    // ==========================

    @GET("api/movies/{movieId}")
    fun getMovieById(
        @Header("Authorization") authorization: String,
        @Path("movieId") movieId: Long
    ): Call<MovieApi>
    @GET("api/showtimes/movie/{movieId}")
    fun getShowtimesByMovie(

        @Header("Authorization")
        authorization: String,

        @Path("movieId")
        movieId: Long

    ): Call<List<ShowtimeApi>>
    @POST("api/auth/register")
    fun register(
        @Body request: RegisterRequest
    ): Call<RegisterResponse>
    @GET("api/showtimes/{showtimeId}/seats")
    fun getSeatsByShowtime(
        @Header("Authorization")
        authorization: String,

        @Path("showtimeId")
        showtimeId: Long
    ): Call<List<SeatApi>>
    @POST("api/bookings/confirm")
    fun confirmBooking(
        @Header("Authorization")
        authorization: String,

        @Body
        request: ConfirmBookingRequest
    ): Call<ConfirmBookingResponse>
// ==========================
// BOOKING CỦA USER
// ==========================

    @GET("api/bookings/user/{userId}")
    fun getBookingsByUser(

        @Header("Authorization")
        authorization: String,

        @Path("userId")
        userId: Long

    ): Call<List<BookingApi>>
    // ==========================
// MY TICKETS
// ==========================

    @GET("api/bookings/user/{userId}/tickets")
    fun getTicketsByUser(

        @Header("Authorization")
        authorization: String,

        @Path("userId")
        userId: Long

    ): Call<List<TicketResponse>>
    // ==========================
// AI CHAT
// ==========================

    @POST("api/ai/chat")
    fun chatWithAi(
        @Header("Authorization")
        authorization: String,

        @Body
        request: AiChatRequest
    ): Call<AiChatResponse>
}


