package com.movie.mylist.database.local

import android.text.TextUtils
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.movie.mylist.database.model.Movie
import com.squareup.moshi.Moshi

@ProvidedTypeConverter
class MovieConverter {
    private val moshi = Moshi.Builder().build()


    @TypeConverter
    fun fromMovie(string: String): Movie? {
        if (TextUtils.isEmpty(string))
            return null
        val jsonAdapter = moshi.adapter(Movie::class.java)
        return jsonAdapter.fromJson(string)
    }

    @TypeConverter
    fun toMovie(user: Movie): String {
        val jsonAdapter = moshi.adapter(Movie::class.java)
        return jsonAdapter.toJson(user)
    }


}