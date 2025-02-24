package com.aaronrubidev.moviebox.data.repository

import com.aaronrubidev.moviebox.domain.model.MovieResponse
import com.aaronrubidev.moviebox.domain.model.Movies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiResponse {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = "55957fcf3ba81b137f8fc01ac5a31fb5",
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): Call<MovieResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String = "55957fcf3ba81b137f8fc01ac5a31fb5"
    ): Call<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = "55957fcf3ba81b137f8fc01ac5a31fb5",
        @Query("language") language: String = "en-US"
    ): Call<Movies>
}