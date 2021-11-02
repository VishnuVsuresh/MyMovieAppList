package com.movie.mylist.ui.main.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.ExperimentalPagingApi
import com.movie.mylist.database.model.Movie

@ExperimentalPagingApi
@Composable
fun NavigationComponent(navController: NavHostController, viewModel: MainViewModel) {

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            MainScreen(navController = navController, viewModel)
        }

        composable("details") {
            val movie = navController.previousBackStackEntry?.savedStateHandle?.get<Movie>("movie")
            movie?.let { it1 -> DetailScreen(navController, movie = it1, viewModel) }
        }
//        composable("details/{movie}", arguments = listOf(
//            navArgument("movie") { type = NavType.ParcelableType(Movie::class.java) }
//        )) { backStackEntry ->
//            backStackEntry?.arguments?.getString("movie")?.let { json ->
//                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//                val movie = moshi.adapter<Movie>(Movie::class.java).fromJson(json)
//                movie?.let { DetailScreen(navController,movie = it) }
//            }
//        }
    }

}