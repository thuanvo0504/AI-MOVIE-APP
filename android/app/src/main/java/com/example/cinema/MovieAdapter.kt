package com.example.cinema.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.cinema.MovieApi
import com.example.cinema.R

class MovieAdapter(

    private val movies: List<MovieApi>,

    private val onMovieClick: (MovieApi) -> Unit

) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {


    // ==========================
    // VIEW HOLDER
    // ==========================

    class MovieViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val poster: ImageView =
            itemView.findViewById(
                R.id.imgPoster
            )

        val title: TextView =
            itemView.findViewById(
                R.id.txtTitle
            )

        val info: TextView =
            itemView.findViewById(
                R.id.txtInfo
            )

        val age: TextView =
            itemView.findViewById(
                R.id.txtAge
            )
    }


    // ==========================
    // TẠO ITEM
    // ==========================

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {

        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_movie,
                    parent,
                    false
                )

        return MovieViewHolder(view)
    }


    // ==========================
    // HIỂN THỊ PHIM
    // ==========================

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {

        val movie =
            movies[position]


        // ==========================
        // POSTER
        // ==========================

        val posterUrl =
            movie.posterUrl

        if (
            posterUrl.isNullOrBlank()
        ) {

            holder.poster
                .setImageDrawable(null)

            Log.e(
                "POSTER_ERROR",
                "Phim ${movie.title} không có POSTER_URL"
            )

        } else {

            Glide.with(
                holder.itemView.context
            )
                .load(posterUrl)
                .centerCrop()
                .listener(

                    object :
                        RequestListener<Drawable> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {

                            Log.e(
                                "POSTER_ERROR",
                                """
                                Không tải được poster
                                Phim: ${movie.title}
                                ID: ${movie.movieId}
                                URL: $posterUrl
                                Lỗi: ${e?.message}
                                """.trimIndent()
                            )

                            return false
                        }


                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {

                            Log.d(
                                "POSTER_OK",
                                "Đã tải poster: ${movie.title}"
                            )

                            return false
                        }
                    }
                )
                .into(
                    holder.poster
                )
        }


        // ==========================
        // TÊN PHIM
        // ==========================

        holder.title.text =
            movie.title


        // ==========================
        // THỂ LOẠI + THỜI LƯỢNG
        // ==========================

        val genre =
            movie.genre
                ?.takeIf {
                    it.isNotBlank()
                }

        holder.info.text =
            if (genre != null) {

                "$genre • ${movie.durationMinutes} phút"

            } else {

                "${movie.durationMinutes} phút"
            }


        // ==========================
        // PHÂN LOẠI TUỔI
        // ==========================

        val ageRating =
            movie.ageRating

        if (
            ageRating.isNullOrBlank()
        ) {

            holder.age.visibility =
                View.GONE

        } else {

            holder.age.visibility =
                View.VISIBLE

            holder.age.text =
                ageRating
        }


        // ==========================
        // BẤM VÀO PHIM
        // ==========================

        holder.itemView
            .setOnClickListener {

                Log.d(
                    "MOVIE_CLICK",
                    "Bấm phim: ${movie.title}, ID=${movie.movieId}"
                )

                onMovieClick(movie)
            }
    }


    // ==========================
    // SỐ LƯỢNG PHIM
    // ==========================

    override fun getItemCount(): Int {

        return movies.size
    }
}