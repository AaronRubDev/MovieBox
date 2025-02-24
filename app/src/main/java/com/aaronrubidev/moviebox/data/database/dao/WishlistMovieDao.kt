package com.aaronrubidev.moviebox.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aaronrubidev.moviebox.data.database.entities.WishlistMovie

@Dao
interface WishlistMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: WishlistMovie)

    @Delete
    suspend fun deleteMovie(movie: WishlistMovie)

    @Query("SELECT * FROM wishlist_movies")
    fun getAllWishlistMovies(): LiveData<List<WishlistMovie>>

    @Query("SELECT * FROM wishlist_movies WHERE id = :movieId LIMIT 1")
    suspend fun getMovieById(movieId: Int): WishlistMovie?
}