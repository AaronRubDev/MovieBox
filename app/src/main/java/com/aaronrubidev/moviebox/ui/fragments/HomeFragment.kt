package com.aaronrubidev.moviebox.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aaronrubidev.moviebox.R
import com.aaronrubidev.moviebox.databinding.FragmentHomeBinding
import com.aaronrubidev.moviebox.domain.model.Movies
import com.aaronrubidev.moviebox.ui.adapter.NowPlayingAdapter
import com.aaronrubidev.moviebox.ui.adapter.PopularMoviesAdapter
import com.aaronrubidev.moviebox.ui.viewmodel.MainViewModel

class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    private val playingNowMoviesAdapter by lazy { NowPlayingAdapter { playingNow -> onPlayingNowClick(playingNow) } }
    private val popularMoviesAdapter by lazy { PopularMoviesAdapter { popularMovie -> onPopularMovieClick(popularMovie) } }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = mBinding.toolbar
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            playingNowMoviesAdapter.setMovies(movies)
            popularMoviesAdapter.addMovies(movies)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            mBinding.nowPlayingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            mBinding.popularMoviesProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            Log.e("HomeFragment", errorMessage)
        }

        mBinding.rvPlayingNowMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = playingNowMoviesAdapter
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mBinding.rvPopularMovies.apply {
            this.layoutManager = layoutManager
            adapter = popularMoviesAdapter
        }

        // Pagination
        mBinding.rvPopularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!viewModel.isLoading.value!! && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    viewModel.loadMoreMovies()
                }
            }
        })
    }

    private fun onPlayingNowClick(movie: Movies) {
        val movieDetailsFragment = MovieDetailsFragment()
        val bundle = Bundle()
        bundle.putInt("movie_id", movie.id) // Pass the movie ID
        movieDetailsFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.navHostFragment, movieDetailsFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun onPopularMovieClick(movie: Movies) {
        val movieDetailsFragment = MovieDetailsFragment()
        val bundle = Bundle()
        bundle.putInt("movie_id", movie.id) // Pass the movie ID
        movieDetailsFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.navHostFragment, movieDetailsFragment)
            .addToBackStack(null)
            .commit()
    }
}
