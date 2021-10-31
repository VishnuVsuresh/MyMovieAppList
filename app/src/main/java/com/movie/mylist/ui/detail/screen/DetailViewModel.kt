package com.movie.mylist.ui.detail.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movie.mylist.database.local.MovieDao
import com.movie.mylist.database.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val movieDao: MovieDao) : ViewModel() {
    private var movieId: Int = -1


    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie>
        get() = _selectedMovie


    fun getMovieById(id: Int) {
        movieId = id
        viewModelScope.launch(Dispatchers.IO) {
            val movie = movieDao.getMovie(id)
            withContext(Dispatchers.Main) {
                _selectedMovie.postValue(movie)
            }

        }
    }


}