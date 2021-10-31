package com.movie.mylist.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.training.pagingcompose.ui.theme.*

private val DarkColorPalette = darkColors(
    primary = white,
    primaryVariant = offwhite,
    secondary = white
)

private val LightColorPalette = lightColors(
    primary = white,
    primaryVariant = offwhite,
    secondary = white
)

@Composable
fun PagingComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}