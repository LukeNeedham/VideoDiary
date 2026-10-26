package com.lukeneedham.videodiary.ui.feature.common.glass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.theme.AppTheme

/** Gradient scrim fading to black at the bottom edge, keeping overlay controls legible over bright video. */
@Composable
fun BottomScrim(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                )
            )
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(Color.White)
                .size(width = 200.dp, height = 120.dp),
        ) {
            BottomScrim(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(60.dp),
            )
        }
    }
}
