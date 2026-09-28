package com.example.cinema

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinema.adapter.MovieAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerMovies:
            RecyclerView

    private lateinit var recyclerUpcoming:
            RecyclerView


    // ==========================
    // ON CREATE
    // ==========================

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContentView(
            R.layout.activity_home
        )


        // ==========================
        // ĐANG CHIẾU
        // ==========================

        recyclerMovies =
            findViewById(
                R.id.recyclerMovies
            )

        recyclerMovies.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )


        // ==========================
        // SẮP CHIẾU
        // ==========================

        recyclerUpcoming =
            findViewById(
                R.id.recyclerUpcoming
            )

        recyclerUpcoming.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )


        // ==========================
        // XEM TẤT CẢ SẮP CHIẾU
        // ==========================

        findViewById<TextView>(
            R.id.btnViewUpcoming
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    UpcomingActivity::class.java
                )
            )
        }


        // ==========================
        // MENU TRANG CHỦ
        // ==========================

        findViewById<TextView>(
            R.id.navHome
        ).setOnClickListener {

            // Đang ở Home
        }


        // ==========================
        // MENU SẮP CHIẾU
        // ==========================

        findViewById<TextView>(
            R.id.navUpcoming
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    UpcomingActivity::class.java
                )
            )
        }


        // ==========================
        // MENU TÌM KIẾM
        // ==========================

        findViewById<TextView>(
            R.id.navSearch
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SearchActivity::class.java
                )
            )
        }


        // ==========================
        // MENU VÉ
        // ==========================

        findViewById<TextView>(
            R.id.navTicket
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MyTicketsActivity::class.java
                )
            )
        }


        // ==========================
        // MENU CHATBOT
        // ==========================

        findViewById<TextView>(
            R.id.navChatbot
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ChatActivity::class.java
                )
            )
        }


        // ==========================
        // MENU TÀI KHOẢN
        // ==========================

        findViewById<TextView>(
            R.id.navAccount
        ).setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AccountActivity::class.java
                )
            )
        }
    }


    // ==========================
    // MỖI LẦN QUAY LẠI HOME
    // TẢI LẠI DỮ LIỆU
    // ==========================

    override fun onResume() {

        super.onResume()

        loadMoviesFromBackend()
    }


    // ==========================
    // LẤY PHIM TỪ BACKEND
    // ==========================

    private fun loadMoviesFromBackend() {

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


        // ==========================
        // KHÔNG CÓ TOKEN
        // ==========================

        if (
            token.isNullOrBlank()
        ) {

            Log.e(
                "MOVIE_API",
                "Không tìm thấy JWT"
            )

            goToLogin()

            return
        }


        // ==========================
        // GỌI API
        // ==========================

        RetrofitClient
            .apiService
            .getMovies(
                "Bearer $token"
            )
            .enqueue(

                object :
                    Callback<List<MovieApi>> {


                    // ==========================
                    // SERVER PHẢN HỒI
                    // ==========================

                    override fun onResponse(
                        call: Call<List<MovieApi>>,
                        response:
                        Response<List<MovieApi>>
                    ) {

                        if (
                            response.isSuccessful
                        ) {

                            val movies =
                                response.body()
                                    ?: emptyList()


                            Log.d(
                                "MOVIE_API",
                                "Tổng số phim: ${movies.size}"
                            )


                            // ==========================
                            // LOG TOÀN BỘ DỮ LIỆU
                            // ==========================

                            movies.forEach { movie ->

                                Log.d(
                                    "MOVIE_DATA",
                                    """
                                    -------------------------
                                    ID=${movie.movieId}
                                    TITLE=${movie.title}
                                    STATUS=${movie.status}
                                    POSTER=${movie.posterUrl}
                                    DESCRIPTION=${movie.description}
                                    GENRE=${movie.genre}
                                    DIRECTOR=${movie.director}
                                    DURATION=${movie.durationMinutes}
                                    AGE=${movie.ageRating}
                                    -------------------------
                                    """.trimIndent()
                                )
                            }


                            // ==========================
                            // ĐANG CHIẾU
                            // ==========================

                            val nowShowing =
                                movies.filter { movie ->

                                    movie.status
                                        .trim()
                                        .equals(
                                            "NOW_SHOWING",
                                            ignoreCase = true
                                        )
                                }


                            // ==========================
                            // SẮP CHIẾU
                            // ==========================

                            val upcoming =
                                movies.filter { movie ->

                                    movie.status
                                        .trim()
                                        .equals(
                                            "UPCOMING",
                                            ignoreCase = true
                                        )
                                }


                            Log.d(
                                "MOVIE_API",
                                "Đang chiếu: ${nowShowing.size}"
                            )

                            Log.d(
                                "MOVIE_API",
                                "Sắp chiếu: ${upcoming.size}"
                            )


                            // ==========================
                            // ADAPTER ĐANG CHIẾU
                            // ==========================

                            recyclerMovies.adapter =
                                MovieAdapter(
                                    nowShowing
                                ) { movie ->

                                    openMovieDetail(
                                        movie
                                    )
                                }


                            // ==========================
                            // ADAPTER SẮP CHIẾU
                            // ==========================

                            recyclerUpcoming.adapter =
                                MovieAdapter(
                                    upcoming
                                ) { movie ->

                                    openMovieDetail(
                                        movie
                                    )
                                }


                        } else {

                            Log.e(
                                "MOVIE_API",
                                "Server trả HTTP ${response.code()}"
                            )


                            if (
                                response.code() == 401 ||
                                response.code() == 403
                            ) {

                                goToLogin()
                            }
                        }
                    }


                    // ==========================
                    // LỖI KẾT NỐI
                    // ==========================

                    override fun onFailure(
                        call: Call<List<MovieApi>>,
                        t: Throwable
                    ) {

                        Log.e(
                            "MOVIE_API",
                            "Không kết nối được Backend: ${t.message}"
                        )
                    }
                }
            )
    }


    // ==========================
    // MỞ CHI TIẾT PHIM
    // ==========================

    private fun openMovieDetail(
        movie: MovieApi
    ) {

        val movieId =
            movie.movieId


        if (
            movieId == null
        ) {

            Log.e(
                "MOVIE_CLICK",
                "movieId bị null: ${movie.title}"
            )

            return
        }


        Log.d(
            "MOVIE_CLICK",
            "Mở phim ${movie.title}, ID=$movieId"
        )


        val intent =
            Intent(
                this,
                MovieDetailActivity::class.java
            )


        intent.putExtra(
            "movie_id",
            movieId
        )


        startActivity(
            intent
        )
    }


    // ==========================
    // VỀ LOGIN
    // ==========================

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