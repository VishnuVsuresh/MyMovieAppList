package com.movie.mylist.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.movie.mylist.ui.theme.onyx
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.format.TextStyle

@ExperimentalCoroutinesApi
@Composable
fun ToolBar(pageTitle: String, navIcon: Int, onBackClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = pageTitle, fontWeight=FontWeight.Bold,textAlign= TextAlign.Center,color = onyx,modifier = Modifier.fillMaxWidth())},
        backgroundColor = Color.Transparent,
        contentColor = Color.White,
        elevation = 0.dp
    )
}