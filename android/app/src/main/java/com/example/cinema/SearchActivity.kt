package com.example.cinema

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinema.adapter.MovieAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerSearchMovies: RecyclerView
    private lateinit var edtSearchMovie: EditText

    private var allMovies: List<MovieApi> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        recyclerSearchMovies =
            findViewById(R.id.recyclerSearchMovies)

        edtSearchMovie =
            findViewById(R.id.edtSearchMovie)

        recyclerSearchMovies.layoutManager =
            LinearLayoutManager(this)

        // Lấy phim thật từ Backend
        loadMovies()

        // ==========================
        // TÌM KIẾM PHIM
        // ==========================

        edtSearchMovie.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    val keyword =
                        s.toString()
                            .trim()

                    filterMovies(keyword)
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )
    }


    // ==========================
    // LẤY PHIM TỪ BACKEND
    // ==========================

    private fun loadMovies() {

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
                "SEARCH_API",
                "Không tìm thấy JWT"
            )

            return
        }

        RetrofitClient.apiService
            .getMovies(
                "Bearer $token"
            )
            .enqueue(
                object :
                    Callback<List<MovieApi>> {

                    override fun onResponse(
                        call: Call<List<MovieApi>>,
                        response: Response<List<MovieApi>>
                    ) {

                        if (response.isSuccessful) {

                            allMovies =
                                response.body()
                                    ?: emptyList()

                            Log.d(
                                "SEARCH_API",
                                "Lấy ${allMovies.size} phim thành công"
                            )

                            showMovies(allMovies)

                        } else {

                            Log.e(
                                "SEARCH_API",
                                "HTTP ${response.code()}"
                            )
                        }
                    }

                    override fun onFailure(
                        call: Call<List<MovieApi>>,
                        t: Throwable
                    ) {

                        Log.e(
                            "SEARCH_API",
                            "Lỗi: ${t.message}"
                        )
                    }
                }
            )
    }


    // ==========================
    // LỌC THEO TÊN
    // ==========================

    private fun filterMovies(
        keyword: String
    ) {

        val filteredMovies =
            if (keyword.isEmpty()) {

                allMovies

            } else {

                allMovies.filter { movie ->

                    movie.title.contains(
                        keyword,
                        ignoreCase = true
                    )
                }
            }

        showMovies(filteredMovies)
    }


    // ==========================
    // HIỂN THỊ DANH SÁCH
    // ==========================

    private fun showMovies(
        movies: List<MovieApi>
    ) {

        recyclerSearchMovies.adapter =
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