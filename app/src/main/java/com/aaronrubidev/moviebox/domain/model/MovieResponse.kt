package com.aaronrubidev.moviebox.domain.model

data class MovieResponse(
    val page: Int,
    val results: List<MovieResult>,
    val total_pages: Int,
    val total_results: Int
)