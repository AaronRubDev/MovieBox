package com.aaronrubidev.moviebox.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aaronrubidev.moviebox.data.database.entities.WishlistMovie
import com.aaronrubidev.moviebox.databinding.FragmentMovieDetailsBinding
import com.aaronrubidev.moviebox.ui.viewmodel.MainViewModel
import com.bumptech.glide.Glide

class MovieDetailsFragment : Fragment() {

    private lateinit var mBinding: FragmentMovieDetailsBinding
    private val viewModel: MainViewModel by viewModels({ requireActivity() })
    private var isMovieAdded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMovieDetailsBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = arguments?.getInt("movie_id")

        if (movieId != null) {
            viewModel.loadMovieDetails(movieId)

            viewModel.checkIfMovieInWishlist(movieId) { isInWishlist ->
                isMovieAdded = isInWishlist
                updateWishListIcons()
            }
        }

        viewModel.movieDetail.observe(viewLifecycleOwner) { movie ->
            movie?.poster_path.let { posterPath ->
                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500" + movie?.poster_path)
                    .into(mBinding.ivPosterMovieDetails)
            }

            mBinding.tvMovieTitleDetails.text = movie?.title
            mBinding.tvReleaseDateDetails.text = movie?.releaseDate
            mBinding.tvOverviewDescription.text = movie?.overview
        }

        mBinding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        mBinding.iconAddToWishList.setOnClickListener {
            viewModel.movieDetail.value?.let { movie ->
                Log.d("MovieDetails", "Adding to wishlist: $movie")
                val wishListMovie = WishlistMovie(
                    id = movie.id,
                    title = movie.title,
                    duration = movie.duration,
                    rating = movie.rating,
                    releaseDate = movie.releaseDate,
                    posterPath = movie.poster_path
                )
                viewModel.addMovieToWishlist(wishListMovie)
                isMovieAdded = true
                updateWishListIcons()
            }
        }

        mBinding.iconRemoveFromWishList.setOnClickListener {
            viewModel.movieDetail.value?.let { movie ->
                val wishListMovie = WishlistMovie(
                    id = movie.id,
                    title = movie.title,
                    duration = movie.duration,
                    rating = movie.rating,
                    releaseDate = movie.releaseDate,
                    posterPath = movie.poster_path
                )
                viewModel.removeMovieFromWishlist(wishListMovie)
                isMovieAdded = false
                updateWishListIcons()
            }
        }
    }

    private fun updateWishListIcons() {
        if (isMovieAdded) {
            mBinding.iconAddToWishList.visibility = View.GONE
            mBinding.iconRemoveFromWishList.visibility = View.VISIBLE
        } else {
            mBinding.iconAddToWishList.visibility = View.VISIBLE
            mBinding.iconRemoveFromWishList.visibility = View.GONE
        }
    }
}