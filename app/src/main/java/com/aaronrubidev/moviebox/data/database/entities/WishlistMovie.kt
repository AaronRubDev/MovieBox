package com.aaronrubidev.moviebox.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_movies")
data class WishlistMovie(
    @PrimaryKey val id: Int,
    val title: String,
    val duration: String?,
    val rating: String?,
    val releaseDate: String?,
    val posterPath: String?
)