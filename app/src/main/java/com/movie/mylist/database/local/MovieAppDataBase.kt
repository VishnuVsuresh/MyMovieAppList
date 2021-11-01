package com.movie.mylist.database.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.movie.mylist.database.model.Movie
import com.movie.mylist.database.model.MovieKey

@Database(entities = [Movie::class, MovieKey::class], version = 1, exportSchema = false)
@TypeConverters(MovieConverter::class)
abstract class MovieAppDataBase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
    abstract fun getMovieKeyDao(): MovieKeyDao
}