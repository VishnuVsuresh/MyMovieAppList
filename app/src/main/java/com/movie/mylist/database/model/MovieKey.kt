package com.movie.mylist.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_key")
class MovieKey (

    @PrimaryKey
    val movieId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)