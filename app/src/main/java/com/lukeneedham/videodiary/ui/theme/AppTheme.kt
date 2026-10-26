package com.lukeneedham.videodiary.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = VideoDiaryColors,
        content = content,
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(AppBackground)
                .padding(16.dp),
        ) {
            Swatch(name = "Accent highlight (primary)", color = AccentHighlight)
            Swatch(name = "Accent accept (secondary)", color = AccentAccept)
            Swatch(name = "Accent record", color = AccentRecord)
            Swatch(name = "Surface", color = AppSurface)
            Swatch(name = "Surface variant", color = AppSurfaceVariant)
        }
    }
}

@Composable
private fun Swatch(name: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(color, CircleShape),
        )
        Text(text = name, color = Color.White)
    }
}
