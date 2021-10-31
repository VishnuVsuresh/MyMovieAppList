package com.movie.mylist.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import com.movie.mylist.R
import com.movie.mylist.ui.main.screen.MainScreen
import com.movie.mylist.ui.main.screen.MainViewModel
import com.movie.mylist.ui.main.screen.NavigationComponent
import com.movie.mylist.ui.theme.PagingComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @ExperimentalPagingApi
    private val mainViewModel: MainViewModel by viewModels()
    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            PagingComposeTheme {
                    NavigationComponent(navController, mainViewModel)
            }
        }

//        setContent {
//
//            PagingComposeTheme {
//                MainScreen(mainViewModel)
//            }
//        }

    }



}