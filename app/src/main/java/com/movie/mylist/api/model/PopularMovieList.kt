package com.movie.mylist.api.model

import com.movie.mylist.database.model.Movie

data class PopularMovieList(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)