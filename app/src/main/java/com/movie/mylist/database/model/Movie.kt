package com.movie.mylist.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity

import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movie_table")
data class Movie(
    @ColumnInfo(name = "adult")
    val adult: Boolean,
    @ColumnInfo(name = "backdrop_path")
    val backdrop_path: String,
    @Transient
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "autoKey")
    val autoId: Int = 0,
    @Json(name = "id")
    val movieId: Int,
    @ColumnInfo(name = "original_language")
    val original_language: String,
    @ColumnInfo(name = "original_title")
    val original_title: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "popularity")
    val popularity: Double,
    @ColumnInfo(name = "poster_path")
    val poster_path: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "vote_average")
    val vote_average: Double,
    @ColumnInfo(name = "vote_count")
    val vote_count: Int,

    @Transient
    @ColumnInfo(name = "favourite")
    var favorite: Boolean = false,

    ) : Parcelable