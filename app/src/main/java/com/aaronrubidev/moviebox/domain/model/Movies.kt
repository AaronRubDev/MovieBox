package com.aaronrubidev.moviebox.domain.model

data class Movies(
    val id: Int,
    val title: String,
    val rating: String,
    val duration: String,
    val releaseDate: String,
    val overview: String? = null,
    val poster_path: String? = null
)
