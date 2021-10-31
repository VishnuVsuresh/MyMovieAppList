package com.movie.mylist.database

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.movie.mylist.api.ApiService
import com.movie.mylist.database.local.MovieDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(private val movieApi: ApiService,private val movieDao: MovieDao) {

    fun getNowPlayingMovies() =
        Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(movieApi,movieDao) }
        ).flow

}