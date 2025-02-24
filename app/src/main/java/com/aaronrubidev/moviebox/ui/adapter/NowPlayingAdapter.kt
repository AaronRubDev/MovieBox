package com.aaronrubidev.moviebox.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.aaronrubidev.moviebox.R
import com.aaronrubidev.moviebox.domain.model.Movies
import com.bumptech.glide.Glide


class NowPlayingAdapter(private val onPlayingNowMoviesClickListener: (Movies) -> Unit) :
    RecyclerView.Adapter<NowPlayingAdapter.NowPlayingViewHolder>() {

    private var movies: MutableList<Movies> = mutableListOf()

    class NowPlayingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playingNowPoster: ImageView = view.findViewById(R.id.ivPlayingNowPoster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_poster_playing_now, parent, false)
        return NowPlayingViewHolder(view)
    }

    override fun onBindViewHolder(holder: NowPlayingViewHolder, position: Int) {
        val movie = movies[position]

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/w500" + movie.poster_path)
            .into(holder.playingNowPoster)

        holder.itemView.setOnClickListener { onPlayingNowMoviesClickListener(movie) }
    }

    override fun getItemCount() = movies.size

    fun setMovies(newMovies: List<Movies>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }
}