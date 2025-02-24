package com.aaronrubidev.moviebox.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaronrubidev.moviebox.R
import com.aaronrubidev.moviebox.data.database.entities.WishlistMovie
import com.aaronrubidev.moviebox.databinding.FragmentWishlistBinding
import com.aaronrubidev.moviebox.ui.adapter.WishlistAdapter
import com.aaronrubidev.moviebox.ui.viewmodel.MainViewModel

class WishlistFragment : Fragment() {

    private lateinit var mBinding: FragmentWishlistBinding
    private val viewModel: MainViewModel by viewModels()
    private val wishlistAdapter by lazy { WishlistAdapter { wishList -> onWishlistClick(wishList) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentWishlistBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        mBinding.rvWishlistMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = wishlistAdapter
        }

        viewModel.wishListMovies.observe(viewLifecycleOwner) { wishlistMovies ->
                wishlistAdapter.setData(wishlistMovies)
        }
    }

    private fun onWishlistClick(wishListMovie: WishlistMovie) {
        val movieDetailsFragment = MovieDetailsFragment()
        val bundle = Bundle()
        bundle.putInt("movie_id", wishListMovie.id) // Pass the movie ID
        movieDetailsFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.navHostFragment, movieDetailsFragment)
            .addToBackStack(null)
            .commit()
    }
}