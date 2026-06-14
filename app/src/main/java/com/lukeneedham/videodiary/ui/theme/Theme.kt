package com.lukeneedham.videodiary.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VideoDiaryColors = darkColors(
    primary = AccentHighlight,
    primaryVariant = AccentHighlight,
    secondary = AccentAccept,
    background = AppBackground,
    surface = AppSurface,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun VideoDiaryTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = VideoDiaryColors,
        content = content,
    )
}
