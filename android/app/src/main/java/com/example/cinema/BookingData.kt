package com.example.cinema

object BookingData {

    // PHIM
    var movieId: Long = -1L
    var movieName: String = ""

    // SUẤT CHIẾU
    var showtimeId: Long = -1L
    var roomId: Long = -1L
    var basePrice: Double = 0.0

    // RẠP / NGÀY / GIỜ
    var cinemaName: String = ""
    var date: String = ""
    var showtime: String = ""

    // GHẾ
    val selectedSeats =
        mutableListOf<String>()

    val selectedShowtimeSeatIds =
        mutableListOf<Long>()

    // TỔNG TIỀN
    var totalPrice: Int = 0

    // BOOKING
    var bookingId: Long = -1L
    var bookingCode: String = ""
    var bookingStatus: String = ""

    // PAYMENT
    var paymentId: Long = -1L
    var transactionCode: String = ""
    var paymentMethod: String = ""
    var paymentStatus: String = ""

    fun clearSeats() {

        selectedSeats.clear()

        selectedShowtimeSeatIds.clear()

        totalPrice = 0
    }
}