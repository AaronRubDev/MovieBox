package com.aaronrubidev.moviebox.data.api

import com.aaronrubidev.moviebox.data.repository.MovieApiResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    val instance: MovieApiResponse by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(MovieApiResponse::class.java)
    }
}