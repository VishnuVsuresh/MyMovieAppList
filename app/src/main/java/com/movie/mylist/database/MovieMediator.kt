package com.movie.mylist.database

import androidx.paging.*
import androidx.room.withTransaction
import com.STARTING_PAGE_INDEX
import com.movie.mylist.api.ApiService
import com.movie.mylist.database.local.MovieAppDataBase
import com.movie.mylist.database.local.MovieDao
import com.movie.mylist.database.model.Movie
import com.movie.mylist.database.model.MovieKey
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class MovieMediator(
    private val movieApi: ApiService,
    private val movieDB: MovieAppDataBase,
) : RemoteMediator<Int, Movie>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = movieApi.getPopularMovieList(position = page)
            val isEndOfList = response.results.isEmpty()
            movieDB.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDB.getMovieDao().deleteAll()
                    movieDB.getMovieKeyDao().deleteAll()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.results.map {
                    MovieKey(it.id, prevKey = prevKey, nextKey = nextKey)
                }
                movieDB.getMovieKeyDao().insertAll(keys)
                movieDB.getMovieDao().insertAll(response.results)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Movie>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): MovieKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                movieDB.getMovieKeyDao().remoteKeysCatId(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Movie>): MovieKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { cat -> movieDB.getMovieKeyDao().remoteKeysCatId(cat.id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Movie>): MovieKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movie -> movieDB.getMovieKeyDao().remoteKeysCatId(movie.id) }
    }
}