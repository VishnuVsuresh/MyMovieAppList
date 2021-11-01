package com.movie.mylist.database.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movie.mylist.database.model.MovieKey

@Dao
interface MovieKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<MovieKey>)

    @Query("SELECT * FROM movie_key WHERE movieId = :id")
    suspend fun remoteKeysMovieId(id: Int): MovieKey?

    @Query("DELETE FROM movie_key")
    suspend fun deleteAll()

    @Query("SELECT * FROM movie_key WHERE movieId = :id")
    suspend fun getMovieRemoteKey(id: Int): MovieKey?

}