package com.movie.mylist.database

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.STARTING_PAGE_INDEX
import com.movie.mylist.api.ApiService
import com.movie.mylist.database.local.MovieAppDataBase
import com.movie.mylist.database.model.Movie
import com.movie.mylist.database.model.MovieKey
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.io.InvalidObjectException

@ExperimentalPagingApi
class MovieMediator(
    private val movieApi: ApiService,
    private val movieDB: MovieAppDataBase,
) : RemoteMediator<Int, Movie>() {
    var page = STARTING_PAGE_INDEX
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        try {
            /*   val pageKeyData = getKeyPageData(loadType, state)
                page = when (pageKeyData) {
                   is MediatorResult.Success -> {
                       return pageKeyData
                   }
                   else -> {
                       pageKeyData as Int
                   }
               }*/

            page = when (loadType) {
                LoadType.REFRESH -> {
                    Timber.e("   key pageKeyData REFRESH")
                    STARTING_PAGE_INDEX
                } // we're refreshing so just reset the page
                LoadType.PREPEND -> {
                    Timber.e("   key pageKeyData PREPEND")
                    return MediatorResult.Success(endOfPaginationReached = true)  // shouldn't need to prepend
                }
                LoadType.APPEND -> {
                    Timber.e("   key pageKeyData APPEND")
                    // work out the next page number
                    page + 1
                }
            }
            Timber.e("   key current page $page")
            val response = movieApi.getPopularMovieList(position = page)
            Timber.e("  key  response  ${response.page} next Page ${response.total_pages}  confi ${state.config.pageSize}")
            val isEndOfList = response.total_pages < response.page
            Timber.e("  key isEndOfList ${isEndOfList} ")

            movieDB.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDB.getMovieKeyDao().deleteAll()
                    movieDB.getMovieDao().deleteAll()
                }
               /* val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val list = response.results?.map {
                    // Timber.e(" keys prev ${it.id}  $prevKey next $nextKey")
                    MovieKey(
                        movieId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        key = response.page
                    )
                }*/

               // movieDB.getMovieKeyDao().insertAll(list)
                movieDB.getMovieDao().insertAll(response.results)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

    }


    /**
     * this returns the page key or the final end of list success result
     */
    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Movie>): Any? {

        return when (loadType) {
            LoadType.REFRESH -> {

                val remoteKeys = getClosestRemoteKeys(state)
                Timber.e("   key REFRESH ${remoteKeys?.nextKey}")
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.APPEND -> {


                val remoteKeys = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("End of result or retry$loadType")
                Timber.e("   key REFRESH ${remoteKeys?.nextKey} ${state.anchorPosition}")
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: throw InvalidObjectException("Invalid state, key should not be null")
                Timber.e("   key REFRESH ${remoteKeys?.nextKey}")
                //end of list condition reached
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }


    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, Movie>): MovieKey? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movie -> movieDB.getMovieKeyDao().getMovieRemoteKey(movie.id) }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKeys(state: PagingState<Int, Movie>): MovieKey? {

        return state.anchorPosition?.let { pos ->
            state.closestItemToPosition(pos)?.let {
                movieDB.getMovieKeyDao().getMovieRemoteKey(it.id)
            }
        }

    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, Movie>): MovieKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                Timber.e("   key getLastRemoteKey lastOrNull ${movie?.id}")
                movieDB.getMovieKeyDao().getMovieRemoteKey(movie.id)
            }
    }


}