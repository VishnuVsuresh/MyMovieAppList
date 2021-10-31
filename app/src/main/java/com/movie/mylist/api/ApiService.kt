package com.movie.mylist.api


import com.movie.mylist.BuildConfig
import com.movie.mylist.api.model.PopularMovieList
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = BuildConfig.MOVIE_API_KEY
    }

    @GET("movie/popular?api_key=$API_KEY")
    suspend fun getPopularMovieList(
        @Query(value = "page") position: Int
    ): PopularMovieList
}