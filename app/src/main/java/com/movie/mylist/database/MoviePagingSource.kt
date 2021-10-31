package com.movie.mylist.database

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.STARTING_PAGE_INDEX
import com.movie.mylist.api.ApiService
import com.movie.mylist.database.local.MovieDao
import com.movie.mylist.database.model.Movie
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException



class MoviePagingSource(
    private val movieApi: ApiService,
    private val movieDao: MovieDao,
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {


        return try {
            val position = params.key ?: STARTING_PAGE_INDEX
            val response = movieApi.getPopularMovieList(position = position)
            val movies = response
            Timber.e("movie page "+response.page)
            Timber.e("movie result"+response.results.size)
          //  movieDao.insertAll(movies.results)
            LoadResult.Page(
                data = movies.results,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position.minus(1),
                nextKey = if (movies.results.isEmpty()) null else position.plus(1)
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}