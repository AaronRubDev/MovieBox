package com.aaronrubidev.moviebox.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aaronrubidev.moviebox.R
import com.aaronrubidev.moviebox.data.database.entities.WishlistMovie
import com.bumptech.glide.Glide

class WishlistAdapter(
    private val wishListMovies: List<WishlistMovie>? = null,
    private val onWishlistClickListener: (WishlistMovie) -> Unit) :
    RecyclerView.Adapter<WishlistAdapter.WishlistAdapterViewHolder>() {

    private var movies: MutableList<WishlistMovie> = mutableListOf()

    class WishlistAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieTitle: TextView = view.findViewById(R.id.tvMovieTitle)
        val duration: TextView = view.findViewById(R.id.tvDuration)
        val rating: TextView = view.findViewById(R.id.tvRating)
        val releaseDate: TextView = view.findViewById(R.id.tvReleaseDate)
        val popularMoviePoster: ImageView = view.findViewById(R.id.ivPoster)
        val progressBarRating: ProgressBar = view.findViewById(R.id.progressBarRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_popular_movie, parent, false)
        return WishlistAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistAdapterViewHolder, position: Int) {
        val movie = movies[position]
        holder.movieTitle.text = movie.title
        holder.duration.text = movie.duration
        holder.rating.text = movie.rating
        holder.releaseDate.text = movie.releaseDate

        val ratingPercentage = movie.rating?.trimEnd('%')?.toIntOrNull() ?: 0
        holder.progressBarRating.progress = ratingPercentage
        holder.rating.text = "${ratingPercentage}%"

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + movie.posterPath)
            .into(holder.popularMoviePoster)

        holder.itemView.setOnClickListener { onWishlistClickListener(movie) }

    }

    override fun getItemCount() = movies.size

    fun setData(wishlistMovies: List<WishlistMovie>) {
        this.movies.clear()
        this.movies.addAll(wishlistMovies)
        notifyDataSetChanged()
    }
}