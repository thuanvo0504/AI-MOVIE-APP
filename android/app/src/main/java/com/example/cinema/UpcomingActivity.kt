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

class UpcomingActivity : AppCompatActivity() {

    private lateinit var recyclerUpcoming: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_upcoming)

        // ==========================
        // RECYCLERVIEW
        // ==========================

        recyclerUpcoming =
            findViewById(R.id.recyclerUpcoming)

        recyclerUpcoming.layoutManager =
            LinearLayoutManager(this)

        // Lấy phim sắp chiếu từ Backend
        loadUpcomingMovies()


        // ==========================
        // NÚT QUAY LẠI
        // ==========================

        findViewById<TextView>(
            R.id.btnBack
        ).setOnClickListener {

            finish()
        }
    }


    // ==========================
    // LẤY PHIM SẮP CHIẾU
    // ==========================

    private fun loadUpcomingMovies() {

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

        if (token == null) {

            Log.e(
                "UPCOMING_API",
                "Không tìm thấy JWT"
            )

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finish()

            return
        }


        RetrofitClient.apiService
            .getMovies(
                "Bearer $token"
            )
            .enqueue(
                object : Callback<List<MovieApi>> {

                    override fun onResponse(
                        call: Call<List<MovieApi>>,
                        response: Response<List<MovieApi>>
                    ) {

                        if (response.isSuccessful) {

                            val movies =
                                response.body()
                                    ?: emptyList()

                            // Chỉ lấy phim UPCOMING
                            val upcomingMovies =
                                movies.filter {

                                    it.status.equals(
                                        "UPCOMING",
                                        ignoreCase = true
                                    )
                                }

                            Log.d(
                                "UPCOMING_API",
                                "Có ${upcomingMovies.size} phim sắp chiếu"
                            )

                            showMovies(
                                upcomingMovies
                            )

                        } else {

                            Log.e(
                                "UPCOMING_API",
                                "Server trả HTTP ${response.code()}"
                            )
                        }
                    }


                    override fun onFailure(
                        call: Call<List<MovieApi>>,
                        t: Throwable
                    ) {

                        Log.e(
                            "UPCOMING_API",
                            "Không lấy được phim: ${t.message}"
                        )
                    }
                }
            )
    }


    // ==========================
    // HIỂN THỊ PHIM
    // ==========================

    private fun showMovies(
        movies: List<MovieApi>
    ) {

        recyclerUpcoming.adapter =
            MovieAdapter(
                movies
            ) { movie ->

                val movieId =
                    movie.movieId
                        ?: return@MovieAdapter

                val intent =
                    Intent(
                        this,
                        MovieDetailActivity::class.java
                    )

                intent.putExtra(
                    "movie_id",
                    movieId
                )

                startActivity(intent)
            }
    }
}