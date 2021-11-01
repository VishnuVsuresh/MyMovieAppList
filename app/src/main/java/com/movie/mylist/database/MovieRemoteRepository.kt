package com.movie.mylist.database

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.movie.mylist.api.ApiService
import com.movie.mylist.database.local.MovieAppDataBase
import com.movie.mylist.database.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

private const val PAGE_SIZE = 20

@Singleton
class MovieRemoteRepository @Inject constructor(
    private val movieApi: ApiService,
    private val movieDB: MovieAppDataBase
) {

    @ExperimentalPagingApi
    fun getMovieFromMediator(): Flow<PagingData<Movie>> {
        val pagingSourceFactory = { movieDB.getMovieDao().getMoviePagingList() }

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE
            ),
            remoteMediator = MovieMediator(
                movieApi = movieApi,
                movieDB = movieDB
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun addToFavourite(id: Int, value: Boolean) {
        movieDB.getMovieDao().addToFavourite(id, value)
    }


    @ExperimentalPagingApi
    val pager = Pager(
        PagingConfig(pageSize = 10),
        remoteMediator = MovieMediator(
            movieApi = movieApi,
            movieDB = movieDB
        )
    ) {
        movieDB.getMovieDao().getMoviePagingList()
    }.flow


}