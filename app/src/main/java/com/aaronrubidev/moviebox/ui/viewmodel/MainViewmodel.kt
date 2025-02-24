package com.aaronrubidev.moviebox.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aaronrubidev.moviebox.data.api.RetrofitClient
import com.aaronrubidev.moviebox.data.database.WishlistDatabase
import com.aaronrubidev.moviebox.data.database.entities.WishlistMovie
import com.aaronrubidev.moviebox.domain.model.Movies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _movies = MutableLiveData<List<Movies>>()
    val movies: LiveData<List<Movies>> = _movies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _nowPlayingMovies = MutableLiveData<List<Movies>>()
    val nowPlayingMovies: LiveData<List<Movies>> = _nowPlayingMovies

    private val _movieDetail = MutableLiveData<Movies?>()
    val movieDetail: MutableLiveData<Movies?> = _movieDetail

    private val wishlistDao = WishlistDatabase.getDatabase(application).WishlistMovieDao()
    val wishListMovies = wishlistDao.getAllWishlistMovies()

    private var currentPage = 1

    init {
        loadNowPlayingMovies()
        loadPopularMovies(1) // Load initial data
    }

    fun loadMoreMovies() {
        currentPage++
        loadPopularMovies(currentPage)
    }

    private fun loadNowPlayingMovies() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Use .execute() for synchronous call
                val response = RetrofitClient.instance.getNowPlayingMovies().execute()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val moviesResponse = response.body()
                        val apiMovies = moviesResponse?.results ?: emptyList()
                        val transformedMovies = apiMovies.map { apiMovies ->
                            Movies(
                                id = apiMovies.id,
                                title = apiMovies.title,
                                rating = "N/A",
                                duration = "",
                                releaseDate = apiMovies.release_date,
                                poster_path = apiMovies.poster_path
                            )
                        }

                        _nowPlayingMovies.value = transformedMovies
                        _isLoading.value = false
                    } else {
                        _errorMessage.value = "Failure: ${response.code()}"
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Failure ${e.message}"
                    _isLoading.value = false
                }
            }
        }
    }

    private fun loadPopularMovies(page: Int) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) { // Launch coroutine in IO thread

            try {
                // Use .execute() for synchronous call
                val response = RetrofitClient.instance.getPopularMovies(page = page).execute()

                withContext(Dispatchers.Main) { // Switch to Main thread to update LiveData
                    if (response.isSuccessful) {
                        val moviesResponse = response.body()
                        val apiMovies = moviesResponse?.results ?: emptyList()
                        val transformedMovies = apiMovies.map { apiMovie ->
                            Movies(
                                id = apiMovie.id,
                                title = apiMovie.title,
                                duration = "120 min",
                                rating = (apiMovie.vote_average * 10).toInt().toString() + "%", // Convert to percentage
                                releaseDate = apiMovie.release_date,
                                poster_path = apiMovie.poster_path
                            )
                        }
                        _movies.value = if (page == 1) {
                            transformedMovies // Replace list for first page
                        } else {
                            _movies.value.orEmpty() + transformedMovies // Append to existing list
                        }

                        _isLoading.value = false
                    } else {
                        _errorMessage.value = "Error: ${response.code()}"
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Failure: ${e.message}"
                    _isLoading.value = false
                }
            }
        }
    }

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.instance.getMovieDetails(movieId).execute()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val movieResponse = response.body()
                        _movieDetail.value = movieResponse

                    } else {
                        val errorMessage = "Error: ${response.code()} - ${response.message()}"
                        _errorMessage.value = errorMessage
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Failure: ${e.message}"
                    _isLoading.value = false
                }
            }
        }
    }

    fun addMovieToWishlist(movie: WishlistMovie) {
        viewModelScope.launch {
            wishlistDao.insertMovie(movie)
        }
    }

    fun removeMovieFromWishlist(movie: WishlistMovie) {
        viewModelScope.launch {
            wishlistDao.deleteMovie(movie)
        }
    }

    fun checkIfMovieInWishlist(movieId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val movie = wishlistDao.getMovieById(movieId)
            withContext(Dispatchers.Main) {
                callback(movie != null)
            }
        }
    }
}
