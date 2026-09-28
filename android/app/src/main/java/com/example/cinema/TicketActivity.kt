package com.example.cinema

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class TicketActivity :
    AppCompatActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_ticket
        )


        // ==========================================
        // VIEW
        // ==========================================

        val tvTicketMessage =
            findViewById<TextView>(
                R.id.tvTicketMessage
            )

        val tvBookingCode =
            findViewById<TextView>(
                R.id.tvBookingCode
            )

        val tvMovieName =
            findViewById<TextView>(
                R.id.tvMovieName
            )

        val tvCinema =
            findViewById<TextView>(
                R.id.tvCinema
            )

        val tvDate =
            findViewById<TextView>(
                R.id.tvDate
            )

        val tvTime =
            findViewById<TextView>(
                R.id.tvTime
            )

        val tvSeats =
            findViewById<TextView>(
                R.id.tvSeats
            )

        val tvPaymentMethod =
            findViewById<TextView>(
                R.id.tvPaymentMethod
            )

        val tvBookingStatus =
            findViewById<TextView>(
                R.id.tvBookingStatus
            )

        val tvPaymentStatus =
            findViewById<TextView>(
                R.id.tvPaymentStatus
            )

        val tvTotalPrice =
            findViewById<TextView>(
                R.id.tvTotalPrice
            )

        val tvTransactionCode =
            findViewById<TextView>(
                R.id.tvTransactionCode
            )

        val tvNote =
            findViewById<TextView>(
                R.id.tvNote
            )

        val btnHome =
            findViewById<Button>(
                R.id.btnHome
            )


        // ==========================================
        // HIỂN THỊ MÃ VÉ
        // ==========================================

        tvBookingCode.text =
            if (
                BookingData.bookingCode
                    .isNotBlank()
            ) {

                BookingData.bookingCode

            } else {

                "CHƯA CÓ MÃ VÉ"
            }


        // ==========================================
        // PHIM
        // ==========================================

        tvMovieName.text =
            if (
                BookingData.movieName
                    .isNotBlank()
            ) {

                BookingData.movieName

            } else {

                "Không xác định"
            }


        // ==========================================
        // RẠP
        // ==========================================

        tvCinema.text =
            if (
                BookingData.cinemaName
                    .isNotBlank()
            ) {

                BookingData.cinemaName

            } else {

                "Không xác định"
            }


        // ==========================================
        // NGÀY
        // ==========================================

        tvDate.text =
            if (
                BookingData.date
                    .isNotBlank()
            ) {

                BookingData.date

            } else {

                "--/--/----"
            }


        // ==========================================
        // GIỜ
        // ==========================================

        tvTime.text =
            if (
                BookingData.showtime
                    .isNotBlank()
            ) {

                BookingData.showtime

            } else {

                "--:--"
            }


        // ==========================================
        // GHẾ
        // ==========================================

        tvSeats.text =
            if (
                BookingData
                    .selectedSeats
                    .isNotEmpty()
            ) {

                BookingData
                    .selectedSeats
                    .joinToString(", ")

            } else {

                "Không có"
            }


        // ==========================================
        // TỔNG TIỀN
        // ==========================================

        val formatter =
            NumberFormat
                .getNumberInstance(
                    Locale("vi", "VN")
                )


        tvTotalPrice.text =
            "${formatter.format(BookingData.totalPrice)}đ"


        // ==========================================
        // PAYMENT METHOD
        // ==========================================

        tvPaymentMethod.text =
            formatPaymentMethod(
                BookingData.paymentMethod
            )


        // ==========================================
        // BOOKING STATUS
        // ==========================================

        tvBookingStatus.text =
            formatBookingStatus(
                BookingData.bookingStatus
            )


        // ==========================================
        // PAYMENT STATUS
        // ==========================================

        tvPaymentStatus.text =
            formatPaymentStatus(
                BookingData.paymentStatus
            )


        // ==========================================
        // TRANSACTION CODE
        // ==========================================

        tvTransactionCode.text =
            if (
                BookingData.transactionCode
                    .isNotBlank()
            ) {

                "Mã giao dịch: ${BookingData.transactionCode}"

            } else {

                "Mã giao dịch: --"
            }


        // ==========================================
        // THÔNG BÁO THEO TRẠNG THÁI
        // ==========================================

        if (
            BookingData.paymentStatus
                .equals(
                    "SUCCESS",
                    ignoreCase = true
                )
        ) {

            tvTicketMessage.text =
                "Thanh toán thành công - Vé đã được xác nhận"

            tvNote.text =
                "Vui lòng xuất trình mã vé khi vào rạp."

        } else {

            tvTicketMessage.text =
                "Đặt vé thành công - Đang chờ thanh toán"

            tvNote.text =
                "Vé đang chờ xác nhận thanh toán."
        }


        // ==========================================
        // VỀ TRANG CHỦ
        // ==========================================

        btnHome.setOnClickListener {

            val intent =
                Intent(
                    this,
                    HomeActivity::class.java
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


    // ==============================================
    // HIỂN THỊ PAYMENT METHOD
    // ==============================================

    private fun formatPaymentMethod(
        method: String
    ): String {

        return when (
            method.uppercase()
        ) {

            "CASH" ->
                "Tiền mặt"

            "CARD" ->
                "Thẻ ngân hàng"

            "E_WALLET" ->
                "Ví điện tử"

            "MOCK" ->
                "Thanh toán mô phỏng"

            else ->
                method.ifBlank {
                    "Không xác định"
                }
        }
    }


    // ==============================================
    // BOOKING STATUS
    // ==============================================

    private fun formatBookingStatus(
        status: String
    ): String {

        return when (
            status.uppercase()
        ) {

            "CONFIRMED" ->
                "ĐÃ XÁC NHẬN"

            "PENDING" ->
                "CHỜ XÁC NHẬN"

            "CANCELLED" ->
                "ĐÃ HỦY"

            else ->
                status.ifBlank {
                    "KHÔNG XÁC ĐỊNH"
                }
        }
    }


    // ==============================================
    // PAYMENT STATUS
    // ==============================================

    private fun formatPaymentStatus(
        status: String
    ): String {

        return when (
            status.uppercase()
        ) {

            "SUCCESS" ->
                "THÀNH CÔNG"

            "PENDING" ->
                "CHỜ THANH TOÁN"

            "FAILED" ->
                "THẤT BẠI"

            "REFUNDED" ->
                "ĐÃ HOÀN TIỀN"

            else ->
                status.ifBlank {
                    "KHÔNG XÁC ĐỊNH"
                }
        }
    }
}