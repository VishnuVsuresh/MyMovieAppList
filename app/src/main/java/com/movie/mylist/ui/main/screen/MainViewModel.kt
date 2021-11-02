package com.movie.mylist.ui.main.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.movie.mylist.database.MovieRemoteRepository
import com.movie.mylist.database.MovieRepository
import com.movie.mylist.database.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalPagingApi
@HiltViewModel
class MainViewModel @Inject constructor(private val remoteRepository: MovieRemoteRepository,private val repository: MovieRepository) :
    ViewModel() {


    // lateinit var movies: Flow<PagingData<Movie>>


//    @ExperimentalPagingApi
//    fun loadMovies() {
//        viewModelScope.launch(Dispatchers.IO) {
//            movies = respository.getMovieFromMediator().cachedIn(viewModelScope)
//        }
//    }


    fun addToFav(id: Int, value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            remoteRepository.addToFavourite(id, value)
        }
    }

    @ExperimentalPagingApi
    val movies: Flow<PagingData<Movie>> =
        remoteRepository.getMovieFromMediator()



    /*val movies: Flow<PagingData<Movie>> =
        repository.getNowPlayingMovies().cachedIn(viewModelScope)*/

//    @ExperimentalPagingApi
//    val movies: Flow<PagingData<Movie>> =
//        respository.pager


}