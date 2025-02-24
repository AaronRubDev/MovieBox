package com.aaronrubidev.moviebox.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aaronrubidev.moviebox.data.database.dao.WishlistMovieDao
import com.aaronrubidev.moviebox.data.database.entities.WishlistMovie
import kotlin.concurrent.Volatile

@Database(entities = [WishlistMovie::class], version = 3, exportSchema = false)
abstract class WishlistDatabase : RoomDatabase() {

    abstract fun WishlistMovieDao(): WishlistMovieDao

    companion object {

        @Volatile
        private var INSTANCE: WishlistDatabase? = null

        private val MIGRATION_1_2 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE wishlist_movies ADD COLUMN new_column TEXT")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("ALTER TABLE wishlist_movies ADD COLUMN duration TEXT")
                database.execSQL("ALTER TABLE wishlist_movies ADD COLUMN rating TEXT")
            }
        }

        fun getDatabase(context: Context): WishlistDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WishlistDatabase::class.java,
                    "movie_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}