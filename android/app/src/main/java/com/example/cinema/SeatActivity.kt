package com.example.cinema

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeatActivity : AppCompatActivity() {

    private lateinit var seatGrid: GridLayout

    private lateinit var txtSelectedSeats: TextView

    private lateinit var txtTotal: TextView

    private lateinit var btnPayment: Button


    // ==========================================
    // DỮ LIỆU SUẤT CHIẾU
    // ==========================================

    private var movieId: Long = -1L

    private var showtimeId: Long = -1L

    private var roomId: Long = -1L

    private var basePrice: Double = 0.0


    // ==========================================
    // GHẾ ĐƯỢC CHỌN
    // Key = SHOWTIME_SEAT_ID
    // ==========================================

    private val selectedSeats =
        linkedMapOf<Long, SeatApi>()


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_seat
        )


        // ==========================================
        // NHẬN DỮ LIỆU TỪ SHOWTIME ACTIVITY
        // ==========================================

        movieId =
            intent.getLongExtra(
                "movie_id",
                -1L
            )

        showtimeId =
            intent.getLongExtra(
                "showtime_id",
                -1L
            )

        roomId =
            intent.getLongExtra(
                "room_id",
                -1L
            )

        basePrice =
            intent.getDoubleExtra(
                "base_price",
                0.0
            )


        Log.d(
            "SEAT_ACTIVITY",
            """
            movie_id=$movieId
            showtime_id=$showtimeId
            room_id=$roomId
            base_price=$basePrice
            """.trimIndent()
        )


        // ==========================================
        // KIỂM TRA SHOWTIME
        // ==========================================

        if (showtimeId == -1L) {

            Toast.makeText(
                this,
                "Không xác định được suất chiếu",
                Toast.LENGTH_SHORT
            ).show()

            finish()

            return
        }


        // ==========================================
        // VIEW
        // ==========================================

        seatGrid =
            findViewById(
                R.id.seatGrid
            )

        txtSelectedSeats =
            findViewById(
                R.id.txtSelectedSeats
            )

        txtTotal =
            findViewById(
                R.id.txtTotal
            )

        btnPayment =
            findViewById(
                R.id.btnPayment
            )


        // ==========================================
        // XÓA GHẾ CŨ
        // ==========================================

        BookingData.clearSeats()

        selectedSeats.clear()

        updateSummary()


        // ==========================================
        // LƯU THÔNG TIN BOOKING
        // ==========================================

        BookingData.movieId =
            movieId

        BookingData.showtimeId =
            showtimeId

        BookingData.roomId =
            roomId

        BookingData.basePrice =
            basePrice


        // ==========================================
        // TẢI GHẾ TỪ BACKEND
        // ==========================================

        loadSeats()


        // ==========================================
        // THANH TOÁN
        // ==========================================

        btnPayment.setOnClickListener {

            if (selectedSeats.isEmpty()) {

                Toast.makeText(
                    this,
                    "Vui lòng chọn ít nhất một ghế",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            // Lưu tên ghế
            BookingData.selectedSeats.clear()

            BookingData.selectedSeats.addAll(

                selectedSeats.values.map {
                    "${it.rowName}${it.seatNumber}"
                }
            )


            // Lưu SHOWTIME_SEAT_ID
            BookingData
                .selectedShowtimeSeatIds
                .clear()

            BookingData
                .selectedShowtimeSeatIds
                .addAll(
                    selectedSeats.keys
                )


            // Tổng tiền
            BookingData.totalPrice =
                selectedSeats.values
                    .sumOf {
                        it.price
                    }
                    .toInt()


            Log.d(
                "SEAT_ACTIVITY",
                """
                Đi thanh toán
                showtime_id=$showtimeId
                seats=${BookingData.selectedSeats}
                showtimeSeatIds=${BookingData.selectedShowtimeSeatIds}
                total=${BookingData.totalPrice}
                """.trimIndent()
            )


            startActivity(
                Intent(
                    this,
                    PaymentActivity::class.java
                )
            )
        }
    }


    // ==============================================
    // TẢI GHẾ TỪ BACKEND
    // ==============================================

    private fun loadSeats() {

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


        if (token.isNullOrBlank()) {

            Toast.makeText(
                this,
                "Phiên đăng nhập không hợp lệ",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        RetrofitClient
            .apiService
            .getSeatsByShowtime(
                "Bearer $token",
                showtimeId
            )
            .enqueue(

                object :
                    Callback<List<SeatApi>> {


                    override fun onResponse(
                        call: Call<List<SeatApi>>,
                        response:
                        Response<List<SeatApi>>
                    ) {

                        if (!response.isSuccessful) {

                            Log.e(
                                "SEAT_API",
                                "HTTP ${response.code()}"
                            )

                            Toast.makeText(
                                this@SeatActivity,
                                "Không lấy được danh sách ghế",
                                Toast.LENGTH_SHORT
                            ).show()

                            return
                        }


                        val seats =
                            response.body()
                                ?: emptyList()


                        Log.d(
                            "SEAT_API",
                            "showtime_id=$showtimeId có ${seats.size} ghế"
                        )


                        if (seats.isEmpty()) {

                            Toast.makeText(
                                this@SeatActivity,
                                "Suất chiếu chưa có dữ liệu ghế",
                                Toast.LENGTH_LONG
                            ).show()

                            return
                        }


                        showSeats(
                            seats
                        )
                    }


                    override fun onFailure(
                        call: Call<List<SeatApi>>,
                        t: Throwable
                    ) {

                        Log.e(
                            "SEAT_API",
                            "Lỗi kết nối: ${t.message}",
                            t
                        )

                        Toast.makeText(
                            this@SeatActivity,
                            "Không kết nối được Backend",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
    }


    // ==============================================
    // HIỂN THỊ GHẾ
    // ==============================================

    private fun showSeats(
        seats: List<SeatApi>
    ) {

        seatGrid.removeAllViews()


        // Sắp xếp theo hàng + số ghế
        val sortedSeats =
            seats.sortedWith(
                compareBy<SeatApi> {
                    it.rowName
                }.thenBy {
                    it.seatNumber
                }
            )


        // Số ghế lớn nhất của một hàng
        val maxSeatNumber =
            sortedSeats.maxOfOrNull {
                it.seatNumber
            } ?: 10


        seatGrid.columnCount =
            maxSeatNumber


        sortedSeats.forEach { seat ->

            createSeatButton(
                seat
            )
        }
    }


    // ==============================================
    // TẠO BUTTON CHO 1 GHẾ
    // ==============================================

    private fun createSeatButton(
        seat: SeatApi
    ) {

        val button =
            Button(this)


        val seatName =
            "${seat.rowName}${seat.seatNumber}"


        button.text =
            seatName

        button.textSize =
            10f


        button.setTextColor(
            Color.WHITE
        )


        // ==========================================
        // KÍCH THƯỚC
        // ==========================================

        val params =
            GridLayout.LayoutParams()


        params.width = 0

        params.height =
            dpToPx(48)


        params.columnSpec =
            GridLayout.spec(
                GridLayout.UNDEFINED,
                1f
            )


        val margin =
            dpToPx(2)


        params.setMargins(
            margin,
            margin,
            margin,
            margin
        )


        button.layoutParams =
            params


        // ==========================================
        // TRẠNG THÁI GHẾ
        // ==========================================

        when (
            seat.status.uppercase()
        ) {

            "AVAILABLE" -> {

                button.isEnabled =
                    true

                button.setBackgroundColor(
                    Color.DKGRAY
                )
            }


            "HELD" -> {

                button.isEnabled =
                    false

                button.setBackgroundColor(
                    Color.rgb(
                        180,
                        120,
                        30
                    )
                )
            }


            "BOOKED" -> {

                button.isEnabled =
                    false

                button.setBackgroundColor(
                    Color.rgb(
                        150,
                        40,
                        40
                    )
                )
            }


            else -> {

                button.isEnabled =
                    false

                button.setBackgroundColor(
                    Color.GRAY
                )
            }
        }


        // ==========================================
        // CLICK GHẾ
        // ==========================================

        if (
            seat.status.equals(
                "AVAILABLE",
                ignoreCase = true
            )
        ) {

            button.setOnClickListener {

                val key =
                    seat.showtimeSeatId


                if (
                    selectedSeats.containsKey(
                        key
                    )
                ) {

                    // Bỏ chọn
                    selectedSeats.remove(
                        key
                    )

                    button.setBackgroundColor(
                        Color.DKGRAY
                    )

                } else {

                    // Chọn
                    selectedSeats[key] =
                        seat

                    button.setBackgroundColor(
                        Color.rgb(
                            232,
                            184,
                            75
                        )
                    )
                }


                updateSummary()
            }
        }


        seatGrid.addView(
            button
        )
    }


    // ==============================================
    // CẬP NHẬT GHẾ ĐÃ CHỌN + TỔNG TIỀN
    // ==============================================

    private fun updateSummary() {

        val names =
            selectedSeats.values.map {

                "${it.rowName}${it.seatNumber}"
            }


        txtSelectedSeats.text =
            if (names.isEmpty()) {

                "Ghế đã chọn: Chưa chọn"

            } else {

                "Ghế đã chọn: ${
                    names.joinToString(", ")
                }"
            }


        val total =
            selectedSeats.values
                .sumOf {
                    it.price
                }
                .toLong()


        txtTotal.text =
            "Tổng tiền: ${
                String.format(
                    "%,d",
                    total
                )
            }đ"
    }


    // ==============================================
    // DP → PX
    // ==============================================

    private fun dpToPx(
        dp: Int
    ): Int {

        return (
                dp *
                        resources
                            .displayMetrics
                            .density
                ).toInt()
    }
}