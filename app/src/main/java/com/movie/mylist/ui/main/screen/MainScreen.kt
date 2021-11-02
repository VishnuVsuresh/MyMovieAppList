package com.movie.mylist.ui.main.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.movie.mylist.R
import com.movie.mylist.api.ApiService
import com.movie.mylist.database.model.Movie
import com.movie.mylist.ui.state.ErrorItem
import com.movie.mylist.ui.state.LoadingItem
import com.movie.mylist.ui.state.LoadingView
import com.movie.mylist.ui.theme.colorRed
import com.movie.mylist.ui.theme.onyx
import com.movie.mylist.ui.theme.shapes
import com.movie.mylist.ui.views.ToolBar
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.Flow


@ExperimentalPagingApi
@Composable
fun MainScreen(navController: NavController, mainViewModel: MainViewModel) {
    Scaffold(backgroundColor = Color.Transparent) {
        Column {
            ToolBar(pageTitle = "Movies", navIcon = -1, {

            })
            MovieList(movies = mainViewModel.movies, onItemClick = {
//                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//                val jsonAdapter: JsonAdapter<Movie> =
//                    moshi.adapter(Movie::class.java)
//                val json = jsonAdapter.toJson(it)
//                navController.navigate("details/$json")
                navController.currentBackStackEntry?.savedStateHandle?.set("movie", it)
                navController.navigate("details")

            }, faveClick = { id, fav ->
                mainViewModel.addToFav(id, fav)

            })
        }

    }
}

@Composable
fun MovieList(
    movies: Flow<PagingData<Movie>>,
    onItemClick: (Movie) -> Unit,
    faveClick: (id: Int, isFav: Boolean) -> Unit
) {
    val lazyMovieItems = movies.collectAsLazyPagingItems()
    LazyColumn {

        itemsIndexed(lazyMovieItems) { index, movie ->
            movie?.let { MovieItem(movie = it, onItemClick, faveClick) }
        }

        lazyMovieItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                }
                loadState.append is LoadState.Loading -> {
                    item { LoadingItem() }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = lazyMovieItems.loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = lazyMovieItems.loadState.append as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            onClickRetry = { retry() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    onItemClick: (Movie) -> Unit,
    faveClick: (id: Int, isFav: Boolean) -> Unit
) {
    val checkedState = remember { mutableStateOf(movie.favorite) }
    Box(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(125.dp)
            .clickable { onItemClick?.invoke(movie) }
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier.size(100.dp, 150.dp),
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
                    .weight(1f)
            ) {
                MovieTitle(movie.title!!)
                MovieScore("" + (movie.vote_average * 10).toInt() + "% User Score")

            }

            Icon(
                imageVector = ImageVector.vectorResource(id = if (checkedState.value) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24),
                contentDescription = "", modifier = Modifier
                    .size(20.dp, 20.dp)
                    .clickable {
                        checkedState.value = !movie.favorite
                        faveClick?.invoke(movie.movieId, checkedState.value)
                    }, tint = colorRed
            )
        }


    }

}

@Composable
fun MovieImage(
    imageUrl: String,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    GlideImage(
        imageModel = imageUrl,
        // Crop, Fit, Inside, FillHeight, FillWidth, None
        contentScale = ContentScale.Crop,
        // shows an image with a circular revealed animation.
        circularReveal = CircularReveal(duration = 0),
        // shows a placeholder ImageBitmap when loading.
        placeHolder = ImageVector.vectorResource(id = R.drawable.ic_baseline_image_24),
        // shows an error ImageBitmap when the request failed.
        error = ImageVector.vectorResource(R.drawable.ic_baseline_broken_image_24)

    )
}

@Composable
fun MovieTitle(
    title: String,
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        color = onyx,
        text = title,
        fontWeight = FontWeight.Bold,
        maxLines = 2,

        fontSize = 16.sp,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.h6,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun MovieScore(
    title: String,
) {
    Text(
        color = onyx,
        text = title,
        fontSize = 12.sp,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Normal,
        maxLines = 2,
        style = MaterialTheme.typography.body2,
        overflow = TextOverflow.Ellipsis
    )
}