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
class MainViewModel @Inject constructor(private val respository: MovieRemoteRepository,private val repo: MovieRepository) :
    ViewModel() {


    // lateinit var movies: Flow<PagingData<Movie>>

    init {
        //  loadMovies()
    }


    /*   fun startDetailActivity(activity: Activity, movie: Movie) {
           activity.startActivity(Intent(activity, MovieDetail::class.java)?.apply {
               putExtra("id", movie.id)
           })
       }*/




//    @ExperimentalPagingApi
//    fun loadMovies() {
//        viewModelScope.launch(Dispatchers.IO) {
//            movies = respository.getMovieFromMediator().cachedIn(viewModelScope)
//        }
//    }


    fun addToFav(id: Int, value: Boolean) {

        viewModelScope.launch(Dispatchers.IO) {
            respository.addToFavourite(id, value)
        }

    }

    @ExperimentalPagingApi
    val movies: Flow<PagingData<Movie>> =
        respository.getMovieFromMediator()



    /*val movies: Flow<PagingData<Movie>> =
        repo.getNowPlayingMovies().cachedIn(viewModelScope)*/

//    @ExperimentalPagingApi
//    val movies: Flow<PagingData<Movie>> =
//        respository.pager


}