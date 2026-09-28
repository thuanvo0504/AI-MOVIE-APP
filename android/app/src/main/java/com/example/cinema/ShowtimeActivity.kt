package com.example.cinema

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ShowtimeActivity :
    AppCompatActivity() {


    // ==========================================
    // VIEW
    // ==========================================

    private lateinit var rgCinema:
            RadioGroup

    private lateinit var layoutDays:
            LinearLayout

    private lateinit var layoutTimes:
            LinearLayout

    private lateinit var tvEmptyDate:
            TextView

    private lateinit var tvEmptyTime:
            TextView

    private lateinit var btnContinue:
            Button


    // ==========================================
    // DATA
    // ==========================================

    private var movieId:
            Long = -1L

    private var allShowtimes:
            List<ShowtimeApi> =
        emptyList()

    private var selectedDate:
            LocalDate? =
        null

    private var selectedShowtime:
            ShowtimeApi? =
        null


    private val vietnamZone =
        ZoneId.of(
            "Asia/Ho_Chi_Minh"
        )


    private val dateFormatter =
        DateTimeFormatter.ofPattern(
            "dd/MM/yyyy"
        )


    private val timeFormatter =
        DateTimeFormatter.ofPattern(
            "HH:mm"
        )


    // ==========================================
    // ON CREATE
    // ==========================================

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_showtime
        )


        bindViews()


        movieId =
            intent.getLongExtra(
                "movie_id",
                -1L
            )


        if (movieId <= 0L) {

            Toast.makeText(
                this,
                "Không tìm thấy phim",
                Toast.LENGTH_SHORT
            ).show()

            finish()

            return
        }


        BookingData.movieId =
            movieId


        btnContinue.isEnabled =
            false


        btnContinue.setOnClickListener {

            openSeatActivity()
        }


        loadShowtimes()
    }


    // ==========================================
    // BIND VIEW
    // ==========================================

    private fun bindViews() {

        rgCinema =
            findViewById(
                R.id.rgCinema
            )

        layoutDays =
            findViewById(
                R.id.layoutDays
            )

        layoutTimes =
            findViewById(
                R.id.layoutTimes
            )

        tvEmptyDate =
            findViewById(
                R.id.tvEmptyDate
            )

        tvEmptyTime =
            findViewById(
                R.id.tvEmptyTime
            )

        btnContinue =
            findViewById(
                R.id.btnContinue
            )
    }


    // ==========================================
    // LOAD API
    // ==========================================

    private fun loadShowtimes() {

        tvEmptyDate.visibility =
            View.VISIBLE

        tvEmptyDate.text =
            "Đang tải ngày chiếu..."


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
                "Phiên đăng nhập đã hết",
                Toast.LENGTH_SHORT
            ).show()


            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finish()

            return
        }


        RetrofitClient
            .apiService
            .getShowtimesByMovie(

                "Bearer $token",

                movieId
            )
            .enqueue(

                object :
                    Callback<List<ShowtimeApi>> {


                    override fun onResponse(

                        call:
                        Call<List<ShowtimeApi>>,

                        response:
                        Response<List<ShowtimeApi>>
                    ) {

                        if (
                            !response.isSuccessful
                        ) {

                            showNoShowtimes(
                                "Không tải được suất chiếu\nHTTP ${response.code()}"
                            )

                            return
                        }


                        val serverList =
                            response.body()
                                ?: emptyList()


                        // ==================================
                        // GIỜ THỰC VIỆT NAM
                        // ==================================

                        val now =
                            LocalDateTime.now(
                                vietnamZone
                            )


                        // ==================================
                        // Android lọc thêm một lần nữa.
                        //
                        // Chỉ giữ suất:
                        // START_TIME >= thời gian thực.
                        // ==================================

                        allShowtimes =
                            serverList

                                .filter { showtime ->

                                    val start =
                                        parseDateTime(
                                            showtime.startTime
                                        )

                                    start != null &&
                                            !start.isBefore(
                                                now
                                            )
                                }

                                .sortedBy {

                                    parseDateTime(
                                        it.startTime
                                    )
                                }


                        if (
                            allShowtimes.isEmpty()
                        ) {

                            showNoShowtimes(
                                "Hiện chưa có suất chiếu sắp tới"
                            )

                            return
                        }


                        renderDates()
                    }


                    override fun onFailure(

                        call:
                        Call<List<ShowtimeApi>>,

                        t: Throwable
                    ) {

                        showNoShowtimes(
                            "Không kết nối được server:\n${t.message}"
                        )
                    }
                }
            )
    }


    // ==========================================
    // HIỂN THỊ NGÀY
    // ==========================================

    private fun renderDates() {

        layoutDays.removeAllViews()


        val dates =
            allShowtimes

                .mapNotNull {

                    parseDateTime(
                        it.startTime
                    )
                        ?.toLocalDate()
                }

                .distinct()

                .sorted()

                .take(3)


        if (dates.isEmpty()) {

            showNoShowtimes(
                "Không có ngày chiếu"
            )

            return
        }


        tvEmptyDate.visibility =
            View.GONE


        dates.forEachIndexed {
                index,
                date ->


            val button =
                createOptionButton(
                    date.format(
                        dateFormatter
                    )
                )


            button.setOnClickListener {

                selectedDate =
                    date

                selectedShowtime =
                    null

                btnContinue.isEnabled =
                    false


                renderDates()

                renderTimes(
                    date
                )
            }


            if (
                date == selectedDate
            ) {

                selectButton(
                    button
                )

            } else {

                unselectButton(
                    button
                )
            }


            layoutDays.addView(
                button
            )


            // ==================================
            // Tự chọn ngày đầu tiên.
            // ==================================

            if (
                selectedDate == null &&
                index == 0
            ) {

                selectedDate =
                    date
            }
        }


        selectedDate?.let {

            renderTimes(
                it
            )
        }


        // render lại màu sau khi
        // selectedDate vừa được tạo.

        for (
        i in 0 until layoutDays.childCount
        ) {

            val child =
                layoutDays.getChildAt(
                    i
                ) as Button


            val date =
                dates[i]


            if (
                date == selectedDate
            ) {

                selectButton(
                    child
                )

            } else {

                unselectButton(
                    child
                )
            }
        }
    }


    // ==========================================
    // HIỂN THỊ GIỜ
    // ==========================================

    private fun renderTimes(
        date: LocalDate
    ) {

        layoutTimes.removeAllViews()


        val showtimes =
            allShowtimes

                .filter {

                    parseDateTime(
                        it.startTime
                    )
                        ?.toLocalDate() ==
                            date
                }

                .sortedBy {

                    parseDateTime(
                        it.startTime
                    )
                }


        if (showtimes.isEmpty()) {

            tvEmptyTime.visibility =
                View.VISIBLE

            tvEmptyTime.text =
                "Không có suất chiếu"

            return
        }


        tvEmptyTime.visibility =
            View.GONE


        showtimes.forEach { showtime ->

            val start =
                parseDateTime(
                    showtime.startTime
                )
                    ?: return@forEach


            val button =
                createOptionButton(

                    start.format(
                        timeFormatter
                    )
                )


            button.setOnClickListener {

                selectedShowtime =
                    showtime


                renderTimes(
                    date
                )


                btnContinue.isEnabled =
                    true
            }


            if (
                selectedShowtime
                    ?.showtimeId ==
                showtime.showtimeId
            ) {

                selectButton(
                    button
                )

            } else {

                unselectButton(
                    button
                )
            }


            layoutTimes.addView(
                button
            )
        }
    }


    // ==========================================
    // TẠO BUTTON NGÀY / GIỜ
    // ==========================================

    private fun createOptionButton(
        text: String
    ): Button {

        val button =
            Button(
                this
            )


        button.text =
            text


        button.isAllCaps =
            false


        button.textSize =
            14f


        val params =
            LinearLayout.LayoutParams(

                LinearLayout
                    .LayoutParams
                    .WRAP_CONTENT,

                dp(
                    48
                )
            )


        params.marginEnd =
            dp(
                10
            )


        button.layoutParams =
            params


        button.setPadding(

            dp(18),

            0,

            dp(18),

            0
        )


        return button
    }


    // ==========================================
    // BUTTON SELECT
    // ==========================================

    private fun selectButton(
        button: Button
    ) {

        button.backgroundTintList =
            ColorStateList.valueOf(
                Color.parseColor(
                    "#FFC107"
                )
            )


        button.setTextColor(
            Color.BLACK
        )
    }


    // ==========================================
    // BUTTON KHÔNG SELECT
    // ==========================================

    private fun unselectButton(
        button: Button
    ) {

        button.backgroundTintList =
            ColorStateList.valueOf(
                Color.parseColor(
                    "#25262B"
                )
            )


        button.setTextColor(
            Color.WHITE
        )
    }


    // ==========================================
    // TIẾP TỤC SANG GHẾ
    // ==========================================

    private fun openSeatActivity() {

        val showtime =
            selectedShowtime


        if (showtime == null) {

            Toast.makeText(
                this,
                "Vui lòng chọn suất chiếu",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        val startTime =
            parseDateTime(
                showtime.startTime
            )


        if (startTime == null) {

            Toast.makeText(
                this,
                "Thời gian suất chiếu không hợp lệ",
                Toast.LENGTH_SHORT
            ).show()

            return
        }


        // ======================================
        // LẤY RẠP ĐANG CHỌN
        // ======================================

        val cinemaName =
            when (
                rgCinema.checkedRadioButtonId
            ) {

                R.id.rbLotte ->
                    "Lotte Cinema"

                R.id.rbGalaxy ->
                    "Galaxy Cinema"

                else ->
                    "CGV Vincom"
            }


        // ======================================
        // LƯU BOOKING DATA
        // ======================================

        BookingData.showtimeId =
            showtime.showtimeId


        BookingData.roomId =
            showtime.roomId


        BookingData.basePrice =
            showtime.basePrice


        BookingData.cinemaName =
            cinemaName


        BookingData.date =
            startTime.format(
                dateFormatter
            )


        BookingData.showtime =
            startTime.format(
                timeFormatter
            )


        BookingData.clearSeats()


        // ======================================
        // CHUYỂN SANG SEAT ACTIVITY
        // ======================================

        val intent =
            Intent(
                this,
                SeatActivity::class.java
            )


        intent.putExtra(
            "movie_id",
            movieId
        )


        intent.putExtra(
            "showtime_id",
            showtime.showtimeId
        )


        intent.putExtra(
            "room_id",
            showtime.roomId
        )


        intent.putExtra(
            "base_price",
            showtime.basePrice
        )


        startActivity(
            intent
        )
    }


    // ==========================================
    // PARSE DATETIME
    // ==========================================

    private fun parseDateTime(
        value: String
    ): LocalDateTime? {

        // ======================================
        // Dạng:
        // 2026-09-24T13:00:00
        // ======================================

        try {

            return LocalDateTime.parse(
                value,
                DateTimeFormatter
                    .ISO_LOCAL_DATE_TIME
            )

        } catch (_: Exception) {
        }


        // ======================================
        // Nếu backend sau này trả timezone:
        //
        // 2026-09-24T13:00:00+07:00
        // ======================================

        try {

            return OffsetDateTime
                .parse(value)
                .atZoneSameInstant(
                    vietnamZone
                )
                .toLocalDateTime()

        } catch (_: Exception) {
        }


        return null
    }


    // ==========================================
    // KHÔNG CÓ SUẤT
    // ==========================================

    private fun showNoShowtimes(
        message: String
    ) {

        allShowtimes =
            emptyList()


        layoutDays.removeAllViews()

        layoutTimes.removeAllViews()


        selectedDate =
            null

        selectedShowtime =
            null


        tvEmptyDate.visibility =
            View.VISIBLE

        tvEmptyDate.text =
            message


        tvEmptyTime.visibility =
            View.VISIBLE

        tvEmptyTime.text =
            "Chưa có suất chiếu"


        btnContinue.isEnabled =
            false
    }


    // ==========================================
    // DP
    // ==========================================

    private fun dp(
        value: Int
    ): Int {

        return (
                value *
                        resources
                            .displayMetrics
                            .density
                )
            .toInt()
    }
}