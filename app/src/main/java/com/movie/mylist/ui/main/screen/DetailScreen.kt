package com.movie.mylist.ui.main.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.ExperimentalPagingApi
import com.movie.mylist.R
import com.movie.mylist.api.ApiService
import com.movie.mylist.database.model.Movie
import com.movie.mylist.ui.theme.colorRed
import com.movie.mylist.ui.theme.onyx
import com.movie.mylist.ui.theme.shapes

@ExperimentalPagingApi
@Composable
fun DetailScreen(navController: NavHostController, movie: Movie, viewModel: MainViewModel) {
    val checkedState = remember { mutableStateOf(movie.favorite) }
    Scaffold(backgroundColor = Color.Transparent) {
        Column {
            TopAppBar(
                title = {
                    Text(
                        text = movie.title,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        color = onyx,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                backgroundColor = Color.Transparent,
                contentColor = Color.White,
                elevation = 0.dp,
                navigationIcon = {
                    IconButton(onClick = {
                       navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_arrow_back_24),
                            contentDescription = "", tint = onyx
                        )
                    }
                },actions = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id =if(checkedState.value)R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24),
                        contentDescription = "",tint= colorRed,modifier = Modifier.clickable {
                            checkedState.value=!checkedState.value
                            viewModel.addToFav(movie.movieId,checkedState.value)
                        }
                    )
                }
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                elevation = 2.dp,
                shape = shapes.medium
            ) {
                MovieImage(
                    ApiService.IMAGE_URL + movie.poster_path,
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(16.dp, 16.dp)
            ) {
                MovieTitle(movie.title)
                Spacer(Modifier.height(10.dp))
                MovieScore(movie.overview)

            }

        }

    }
}