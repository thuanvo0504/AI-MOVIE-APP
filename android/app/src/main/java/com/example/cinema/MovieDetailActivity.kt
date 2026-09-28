package com.example.cinema

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailActivity : AppCompatActivity() {

    private var movieId: Long = -1L


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_movie_detail
        )


        // ==========================================
        // NHẬN MOVIE ID
        // ==========================================

        movieId =
            intent.getLongExtra(
                "movie_id",
                -1L
            )


        Log.d(
            "MOVIE_DETAIL",
            "Nhận movie_id = $movieId"
        )


        // ==========================================
        // KIỂM TRA MOVIE ID
        // ==========================================

        if (movieId == -1L) {

            Log.e(
                "MOVIE_DETAIL",
                "Không nhận được movie_id"
            )

            Toast.makeText(
                this,
                "Không xác định được phim",
                Toast.LENGTH_SHORT
            ).show()

            finish()

            return
        }


        // ==========================================
        // LẤY CHI TIẾT PHIM
        // ==========================================

        loadMovieDetail(
            movieId
        )


        // ==========================================
        // NÚT ĐẶT VÉ
        // ==========================================

        findViewById<Button>(
            R.id.btnBooking
        ).setOnClickListener {

            val intent =
                Intent(
                    this,
                    ShowtimeActivity::class.java
                )


            // Truyền movie_id sang ShowtimeActivity
            intent.putExtra(
                "movie_id",
                movieId
            )


            Log.d(
                "MOVIE_BOOKING",
                "Đặt vé movie_id=$movieId, movieName=${BookingData.movieName}"
            )


            startActivity(
                intent
            )
        }
    }


    // ==============================================
    // LẤY CHI TIẾT PHIM TỪ BACKEND
    // ==============================================

    private fun loadMovieDetail(
        id: Long
    ) {

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


        // ==========================================
        // KHÔNG CÓ JWT
        // ==========================================

        if (token.isNullOrBlank()) {

            Log.e(
                "MOVIE_DETAIL",
                "Không tìm thấy JWT"
            )

            goToLogin()

            return
        }


        // ==========================================
        // GỌI API
        // ==========================================

        RetrofitClient
            .apiService
            .getMovieById(
                "Bearer $token",
                id
            )
            .enqueue(

                object :
                    Callback<MovieApi> {


                    // ==================================
                    // BACKEND PHẢN HỒI
                    // ==================================

                    override fun onResponse(
                        call: Call<MovieApi>,
                        response: Response<MovieApi>
                    ) {

                        if (response.isSuccessful) {

                            val movie =
                                response.body()


                            if (movie != null) {

                                Log.d(
                                    "MOVIE_DETAIL",
                                    "Lấy phim thành công: ${movie.title}"
                                )


                                // ==========================
                                // LƯU PHIM VÀO BOOKING DATA
                                // ==========================

                                BookingData.movieId =
                                    movie.movieId
                                        ?: movieId

                                BookingData.movieName =
                                    movie.title


                                Log.d(
                                    "BOOKING_DATA",
                                    """
                                    movieId=${BookingData.movieId}
                                    movieName=${BookingData.movieName}
                                    """.trimIndent()
                                )


                                // ==========================
                                // HIỂN THỊ PHIM
                                // ==========================

                                showMovie(
                                    movie
                                )

                            } else {

                                Log.e(
                                    "MOVIE_DETAIL",
                                    "Backend trả dữ liệu rỗng"
                                )

                                Toast.makeText(
                                    this@MovieDetailActivity,
                                    "Không tìm thấy thông tin phim",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {

                            Log.e(
                                "MOVIE_DETAIL",
                                "HTTP ${response.code()}"
                            )


                            // JWT hết hạn hoặc không hợp lệ
                            if (
                                response.code() == 401 ||
                                response.code() == 403
                            ) {

                                goToLogin()

                            } else {

                                Toast.makeText(
                                    this@MovieDetailActivity,
                                    "Không lấy được thông tin phim",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }


                    // ==================================
                    // LỖI KẾT NỐI
                    // ==================================

                    override fun onFailure(
                        call: Call<MovieApi>,
                        t: Throwable
                    ) {

                        Log.e(
                            "MOVIE_DETAIL",
                            "Không lấy được phim: ${t.message}",
                            t
                        )


                        Toast.makeText(
                            this@MovieDetailActivity,
                            "Không kết nối được Backend",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
    }


    // ==============================================
    // HIỂN THỊ THÔNG TIN PHIM
    // ==============================================

    private fun showMovie(
        movie: MovieApi
    ) {

        // ==========================================
        // POSTER
        // ==========================================

        val imgPoster =
            findViewById<ImageView>(
                R.id.imgPoster
            )


        Glide.with(this)
            .load(movie.posterUrl)
            .centerCrop()
            .into(imgPoster)


        // ==========================================
        // TÊN PHIM
        // ==========================================

        findViewById<TextView>(
            R.id.txtTitle
        ).text =
            movie.title


        // ==========================================
        // THỂ LOẠI + THỜI LƯỢNG
        // ==========================================

        val genre =
            movie.genre
                ?.takeIf {
                    it.isNotBlank()
                }
                ?: "Chưa cập nhật"


        findViewById<TextView>(
            R.id.txtInfo
        ).text =
            "$genre • ${movie.durationMinutes} phút"


        // ==========================================
        // ĐẠO DIỄN
        // ==========================================

        findViewById<TextView>(
            R.id.txtDirector
        ).text =
            movie.director
                ?.takeIf {
                    it.isNotBlank()
                }
                ?: "Chưa cập nhật"


        // ==========================================
        // DIỄN VIÊN
        // ==========================================

        findViewById<TextView>(
            R.id.txtActors
        ).text =
            movie.actors
                ?.takeIf {
                    it.isNotBlank()
                }
                ?: "Chưa cập nhật"


        // ==========================================
        // NGÀY KHỞI CHIẾU
        // ==========================================

        findViewById<TextView>(
            R.id.txtReleaseDate
        ).text =
            formatReleaseDate(
                movie.releaseDate
            )


        // ==========================================
        // NGÔN NGỮ
        // ==========================================

        findViewById<TextView>(
            R.id.txtLanguage
        ).text =
            movie.language
                ?.takeIf {
                    it.isNotBlank()
                }
                ?: "Chưa cập nhật"


        // ==========================================
        // PHÂN LOẠI TUỔI
        // ==========================================

        val ageRating =
            movie.ageRating
                ?.takeIf {
                    it.isNotBlank()
                }
                ?: "Chưa cập nhật"


        findViewById<TextView>(
            R.id.txtAgeRating
        ).text =
            ageRating


        // ==========================================
        // GIẢI THÍCH PHÂN LOẠI
        // ==========================================

        findViewById<TextView>(
            R.id.txtCertification
        ).text =
            getAgeDescription(
                ageRating
            )


        // ==========================================
        // NỘI DUNG PHIM
        // ==========================================

        findViewById<TextView>(
            R.id.txtDescription
        ).text =
            movie.description
                ?.takeIf {
                    it.isNotBlank()
                }
                ?: "Chưa có nội dung phim"
    }


    // ==============================================
    // ĐỊNH DẠNG NGÀY
    // yyyy-MM-dd -> dd/MM/yyyy
    // ==============================================

    private fun formatReleaseDate(
        date: String?
    ): String {

        if (date.isNullOrBlank()) {

            return "Chưa cập nhật"
        }


        return try {

            // Backend có thể trả:
            // 2026-09-18
            // hoặc:
            // 2026-09-18T00:00:00

            val cleanDate =
                date.substringBefore("T")


            val parts =
                cleanDate.split("-")


            if (parts.size == 3) {

                "${parts[2]}/${parts[1]}/${parts[0]}"

            } else {

                date
            }

        } catch (e: Exception) {

            date
        }
    }


    // ==============================================
    // GIẢI THÍCH PHÂN LOẠI TUỔI
    // ==============================================

    private fun getAgeDescription(
        ageRating: String
    ): String {

        return when (
            ageRating.uppercase()
        ) {

            "P" ->
                "Phim được phép phổ biến đến người xem ở mọi độ tuổi."


            "K" ->
                "Phim dành cho người dưới 13 tuổi với điều kiện xem cùng cha, mẹ hoặc người giám hộ."


            "T13" ->
                "Phim dành cho người xem từ đủ 13 tuổi trở lên."


            "T16" ->
                "Phim dành cho người xem từ đủ 16 tuổi trở lên."


            "T18" ->
                "Phim dành cho người xem từ đủ 18 tuổi trở lên."


            else ->
                "Chưa có thông tin phân loại."
        }
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