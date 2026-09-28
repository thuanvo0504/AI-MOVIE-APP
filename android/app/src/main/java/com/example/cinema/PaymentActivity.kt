package com.example.cinema

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {

    private lateinit var rgPayment: RadioGroup
    private lateinit var imgQrPayment: ImageView
    private lateinit var btnConfirm: Button

    private var paymentMethod: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_payment
        )


        // ==========================================
        // ÁNH XẠ VIEW
        // ==========================================

        val txtMovie =
            findViewById<TextView>(
                R.id.txtMovie
            )

        val txtCinema =
            findViewById<TextView>(
                R.id.txtCinema
            )

        val txtShowtime =
            findViewById<TextView>(
                R.id.txtShowtime
            )

        val txtSeats =
            findViewById<TextView>(
                R.id.txtSeats
            )

        val txtTotal =
            findViewById<TextView>(
                R.id.txtPaymentTotal
            )

        rgPayment =
            findViewById(
                R.id.rgPayment
            )

        imgQrPayment =
            findViewById(
                R.id.imgQrPayment
            )

        btnConfirm =
            findViewById(
                R.id.btnConfirmPayment
            )


        // ==========================================
        // HIỂN THỊ THÔNG TIN VÉ
        // ==========================================

        txtMovie.text =
            "Phim: ${BookingData.movieName}"

        txtCinema.text =
            "Rạp: ${BookingData.cinemaName}"

        txtShowtime.text =
            "Suất: ${BookingData.date} - ${BookingData.showtime}"

        txtSeats.text =
            "Ghế: ${
                BookingData.selectedSeats
                    .joinToString(", ")
            }"

        txtTotal.text =
            "Tổng tiền: ${
                String.format(
                    "%,d",
                    BookingData.totalPrice
                )
            }đ"


        // QR mặc định ẩn
        imgQrPayment.visibility =
            View.GONE


        // ==========================================
        // KIỂM TRA DỮ LIỆU BOOKING
        // ==========================================

        Log.d(
            "PAYMENT_DATA",
            """
            movieId=${BookingData.movieId}
            movieName=${BookingData.movieName}
            showtimeId=${BookingData.showtimeId}
            roomId=${BookingData.roomId}
            seats=${BookingData.selectedSeats}
            showtimeSeatIds=${BookingData.selectedShowtimeSeatIds}
            total=${BookingData.totalPrice}
            """.trimIndent()
        )


        // ==========================================
        // CHỌN PHƯƠNG THỨC THANH TOÁN
        // ==========================================

        rgPayment.setOnCheckedChangeListener {
                _,
                checkedId ->

            when (checkedId) {

                // ==================================
                // VÍ ĐIỆN TỬ
                // ==================================

                R.id.rbMomo -> {

                    paymentMethod =
                        "E_WALLET"

                    imgQrPayment.visibility =
                        View.VISIBLE
                }


                // ==================================
                // THẺ NGÂN HÀNG
                // ==================================

                R.id.rbBank -> {

                    paymentMethod =
                        "CARD"

                    imgQrPayment.visibility =
                        View.GONE
                }


                // ==================================
                // THANH TOÁN TẠI QUẦY
                // ==================================

                R.id.rbCash -> {

                    paymentMethod =
                        "CASH"

                    imgQrPayment.visibility =
                        View.GONE
                }


                else -> {

                    paymentMethod =
                        ""

                    imgQrPayment.visibility =
                        View.GONE
                }
            }


            Log.d(
                "PAYMENT_METHOD",
                "Phương thức=$paymentMethod"
            )
        }


        // ==========================================
        // XÁC NHẬN THANH TOÁN
        // ==========================================

        btnConfirm.setOnClickListener {

            confirmPayment()
        }
    }


    // ==============================================
    // XÁC NHẬN THANH TOÁN
    // ==============================================

    private fun confirmPayment() {

        // ==========================================
        // 1. KIỂM TRA PHƯƠNG THỨC
        // ==========================================

        if (paymentMethod.isBlank()) {

            Toast.makeText(
                this,
                "Vui lòng chọn phương thức thanh toán",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        // ==========================================
        // 2. KIỂM TRA SHOWTIME
        // ==========================================

        if (BookingData.showtimeId <= 0) {

            Toast.makeText(
                this,
                "Không xác định được suất chiếu",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        // ==========================================
        // 3. KIỂM TRA GHẾ
        // ==========================================

        if (
            BookingData
                .selectedShowtimeSeatIds
                .isEmpty()
        ) {

            Toast.makeText(
                this,
                "Không có ghế được chọn",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        // ==========================================
        // 4. LẤY USER ID + TOKEN
        // ==========================================

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

            Toast.makeText(
                this,
                "Phiên đăng nhập không hợp lệ",
                Toast.LENGTH_SHORT
            ).show()

            goToLogin()

            return
        }


        // ==========================================
        // 5. TẠO REQUEST
        // ==========================================

        val request =
            ConfirmBookingRequest(

                userId =
                    userId,

                showtimeId =
                    BookingData.showtimeId,

                showtimeSeatIds =
                    BookingData
                        .selectedShowtimeSeatIds
                        .toList(),

                paymentMethod =
                    paymentMethod
            )


        Log.d(
            "PAYMENT_API",
            """
            userId=${request.userId}
            showtimeId=${request.showtimeId}
            seats=${request.showtimeSeatIds}
            paymentMethod=${request.paymentMethod}
            """.trimIndent()
        )


        // ==========================================
        // 6. KHÓA NÚT TRÁNH BẤM 2 LẦN
        // ==========================================

        btnConfirm.isEnabled =
            false

        btnConfirm.text =
            "ĐANG XỬ LÝ..."


        // ==========================================
        // 7. GỌI BACKEND
        // ==========================================

        RetrofitClient
            .apiService
            .confirmBooking(
                "Bearer $token",
                request
            )
            .enqueue(

                object :
                    Callback<ConfirmBookingResponse> {


                    override fun onResponse(
                        call:
                        Call<ConfirmBookingResponse>,

                        response:
                        Response<ConfirmBookingResponse>
                    ) {

                        // ==================================
                        // THÀNH CÔNG
                        // ==================================

                        if (response.isSuccessful) {

                            val result =
                                response.body()


                            if (result == null) {

                                restoreButton()

                                Toast.makeText(
                                    this@PaymentActivity,
                                    "Backend không trả dữ liệu đặt vé",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return
                            }


                            Log.d(
                                "PAYMENT_API",
                                """
                                ĐẶT VÉ THÀNH CÔNG
                                bookingId=${result.bookingId}
                                bookingCode=${result.bookingCode}
                                paymentId=${result.paymentId}
                                transactionCode=${result.transactionCode}
                                totalAmount=${result.totalAmount}
                                bookingStatus=${result.bookingStatus}
                                paymentStatus=${result.paymentStatus}
                                """.trimIndent()
                            )


                            // ==========================
                            // LƯU KẾT QUẢ
                            // ==========================

                            BookingData.bookingId =
                                result.bookingId

                            BookingData.bookingCode =
                                result.bookingCode

                            BookingData.paymentId =
                                result.paymentId

                            BookingData.transactionCode =
                                result.transactionCode

                            BookingData.paymentMethod =
                                paymentMethod

                            BookingData.paymentStatus =
                                result.paymentStatus

                            BookingData.bookingStatus =
                                result.bookingStatus

                            BookingData.totalPrice =
                                result.totalAmount
                                    .toInt()


                            // ==========================
                            // THÔNG BÁO
                            // ==========================

                            val message =
                                if (
                                    result.paymentStatus
                                        .equals(
                                            "SUCCESS",
                                            ignoreCase = true
                                        )
                                ) {

                                    "Đặt vé và thanh toán thành công"

                                } else {

                                    "Đặt vé thành công - chờ thanh toán tại quầy"
                                }


                            Toast.makeText(
                                this@PaymentActivity,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()


                            // ==========================
                            // MỞ VÉ
                            // ==========================

                            openTicket()

                            return
                        }


                        // ==================================
                        // JWT HẾT HẠN
                        // ==================================

                        if (
                            response.code() == 401 ||
                            response.code() == 403
                        ) {

                            restoreButton()

                            Toast.makeText(
                                this@PaymentActivity,
                                "Phiên đăng nhập đã hết hạn",
                                Toast.LENGTH_SHORT
                            ).show()

                            goToLogin()

                            return
                        }


                        // ==================================
                        // GHẾ ĐÃ ĐƯỢC NGƯỜI KHÁC ĐẶT
                        // ==================================

                        if (
                            response.code() == 409
                        ) {

                            restoreButton()

                            Toast.makeText(
                                this@PaymentActivity,
                                "Một hoặc nhiều ghế vừa được người khác đặt",
                                Toast.LENGTH_LONG
                            ).show()

                            Log.e(
                                "PAYMENT_API",
                                "HTTP 409: ${response.errorBody()?.string()}"
                            )

                            return
                        }


                        // ==================================
                        // REQUEST KHÔNG HỢP LỆ
                        // ==================================

                        if (
                            response.code() == 400
                        ) {

                            restoreButton()

                            val error =
                                response
                                    .errorBody()
                                    ?.string()

                            Log.e(
                                "PAYMENT_API",
                                "HTTP 400: $error"
                            )

                            Toast.makeText(
                                this@PaymentActivity,
                                "Dữ liệu đặt vé không hợp lệ",
                                Toast.LENGTH_LONG
                            ).show()

                            return
                        }


                        // ==================================
                        // LỖI KHÁC
                        // ==================================

                        restoreButton()


                        val error =
                            response
                                .errorBody()
                                ?.string()


                        Log.e(
                            "PAYMENT_API",
                            "HTTP ${response.code()} - $error"
                        )


                        Toast.makeText(
                            this@PaymentActivity,
                            "Thanh toán thất bại: HTTP ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }


                    // ======================================
                    // KHÔNG KẾT NỐI ĐƯỢC BACKEND
                    // ======================================

                    override fun onFailure(
                        call:
                        Call<ConfirmBookingResponse>,

                        t:
                        Throwable
                    ) {

                        restoreButton()


                        Log.e(
                            "PAYMENT_API",
                            "Lỗi kết nối: ${t.message}",
                            t
                        )


                        Toast.makeText(
                            this@PaymentActivity,
                            "Không kết nối được server",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )
    }


    // ==============================================
    // MỞ TICKET
    // ==============================================

    private fun openTicket() {

        val intent =
            Intent(
                this,
                TicketActivity::class.java
            )


        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP


        startActivity(
            intent
        )


        finish()
    }


    // ==============================================
    // KHÔI PHỤC NÚT
    // ==============================================

    private fun restoreButton() {

        btnConfirm.isEnabled =
            true

        btnConfirm.text =
            "XÁC NHẬN THANH TOÁN"
    }


    // ==============================================
    // QUAY VỀ LOGIN
    // ==============================================

    private fun goToLogin() {

        val prefs =
            getSharedPreferences(
                "auth",
                MODE_PRIVATE
            )


        prefs.edit()
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


        startActivity(
            intent
        )


        finish()
    }
}