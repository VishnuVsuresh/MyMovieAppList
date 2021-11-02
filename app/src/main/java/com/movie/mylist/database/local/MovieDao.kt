package com.movie.mylist.database.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.movie.mylist.database.model.Movie

@Dao
interface MovieDao {
    @Insert
    suspend fun addMovie(Movie: Movie)

    @Query("SELECT * FROM movie_table")
    fun getMovieList(): LiveData<List<Movie>>

    @Query("SELECT * FROM movie_table")
    fun getMovieTotalList(): List<Movie>


    @Query("SELECT * FROM movie_table")
    fun getMoviePagingList(): PagingSource<Int,Movie>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<Movie>)

    @Update
    fun update(user: Movie)

    @Delete
    fun delete(user: Movie)

    @Query("Select * from movie_table WHERE movieId = :id")
    fun getMovie(id: Int): Movie

    @Query("DELETE FROM movie_table")
    suspend fun deleteAll()

    @Query("UPDATE movie_table SET favourite=:favorite WHERE movieId = :id")
    suspend fun addToFavourite(id: Int, favorite: Boolean)

}