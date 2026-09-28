package com.example.cinema

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class MyTicketsActivity :
    AppCompatActivity() {

    private lateinit var recyclerTickets:
            RecyclerView

    private lateinit var progressTickets:
            ProgressBar

    private lateinit var tvEmptyTickets:
            TextView


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_my_tickets
        )

        recyclerTickets =
            findViewById(
                R.id.recyclerTickets
            )

        progressTickets =
            findViewById(
                R.id.progressTickets
            )

        tvEmptyTickets =
            findViewById(
                R.id.tvEmptyTickets
            )

        recyclerTickets.layoutManager =
            LinearLayoutManager(this)

        loadTickets()
    }


    // ==========================================
    // LẤY DANH SÁCH VÉ
    // ==========================================

    private fun loadTickets() {

        val prefs =
            getSharedPreferences(
                "auth",
                MODE_PRIVATE
            )

        val token =
            prefs.getString(
                "token",
                null
            )

        val userId =
            prefs.getLong(
                "userId",
                -1L
            )


        if (
            token.isNullOrBlank() ||
            userId <= 0
        ) {

            goToLogin()

            return
        }


        progressTickets.visibility =
            View.VISIBLE

        recyclerTickets.visibility =
            View.GONE

        tvEmptyTickets.visibility =
            View.GONE


        RetrofitClient
            .apiService
            .getTicketsByUser(
                "Bearer $token",
                userId
            )
            .enqueue(

                object :
                    Callback<List<TicketResponse>> {


                    override fun onResponse(
                        call: Call<List<TicketResponse>>,
                        response: Response<List<TicketResponse>>
                    ) {

                        progressTickets.visibility =
                            View.GONE


                        if (
                            response.code() == 401 ||
                            response.code() == 403
                        ) {

                            goToLogin()

                            return
                        }


                        if (response.isSuccessful) {

                            val tickets =
                                response.body()
                                    ?: emptyList()


                            if (tickets.isEmpty()) {

                                tvEmptyTickets.text =
                                    "Bạn chưa có vé nào"

                                tvEmptyTickets.visibility =
                                    View.VISIBLE

                                recyclerTickets.visibility =
                                    View.GONE

                            } else {

                                recyclerTickets.adapter =
                                    TicketAdapter(
                                        tickets
                                    ) { ticket ->

                                        openTicket(
                                            ticket
                                        )
                                    }

                                recyclerTickets.visibility =
                                    View.VISIBLE
                            }

                        } else {

                            tvEmptyTickets.text =
                                "Không thể tải danh sách vé"

                            tvEmptyTickets.visibility =
                                View.VISIBLE
                        }
                    }


                    override fun onFailure(
                        call: Call<List<TicketResponse>>,
                        t: Throwable
                    ) {

                        progressTickets.visibility =
                            View.GONE

                        recyclerTickets.visibility =
                            View.GONE

                        tvEmptyTickets.text =
                            "Không kết nối được máy chủ"

                        tvEmptyTickets.visibility =
                            View.VISIBLE

                        Toast.makeText(
                            this@MyTicketsActivity,
                            "Lỗi: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )
    }


    // ==========================================
    // MỞ CHI TIẾT VÉ
    // ==========================================

    private fun openTicket(
        ticket: TicketResponse
    ) {

        // Xóa dữ liệu ghế cũ
        BookingData.clearSeats()


        // BOOKING
        BookingData.bookingId =
            ticket.bookingId

        BookingData.bookingCode =
            ticket.bookingCode

        BookingData.bookingStatus =
            ticket.bookingStatus


        // PHIM
        BookingData.movieName =
            ticket.movieName


        // RẠP + PHÒNG
        BookingData.cinemaName =
            "${ticket.cinemaName} - ${ticket.roomName}"


        // NGÀY + GIỜ
        BookingData.date =
            ticket.date

        BookingData.showtime =
            ticket.showtime


        // GHẾ
        val seats =
            ticket.seats
                .split(",")
                .map {
                    it.trim()
                }
                .filter {
                    it.isNotBlank()
                }

        BookingData
            .selectedSeats
            .addAll(
                seats
            )


        // TỔNG TIỀN
        BookingData.totalPrice =
            ticket.totalAmount
                .roundToInt()


        // PAYMENT
        BookingData.paymentMethod =
            ticket.paymentMethod
                ?: ""

        BookingData.paymentStatus =
            ticket.paymentStatus
                ?: ""

        BookingData.transactionCode =
            ticket.transactionCode
                ?: ""


        val intent =
            Intent(
                this,
                TicketActivity::class.java
            )

        startActivity(
            intent
        )
    }


    // ==========================================
    // QUAY VỀ LOGIN
    // ==========================================

    private fun goToLogin() {

        getSharedPreferences(
            "auth",
            MODE_PRIVATE
        )
            .edit()
            .clear()
            .apply()


        val intent =
            Intent(
                this,
                LoginActivity::class.java
            )

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        finish()
    }
}