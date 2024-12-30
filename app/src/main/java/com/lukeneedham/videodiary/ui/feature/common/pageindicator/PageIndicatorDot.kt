package com.lukeneedham.videodiary.ui.feature.common.pageindicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PageIndicatorDot(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(color = color, shape = CircleShape))
}

@Preview
@Composable
private fun PreviewPageIndicatorDot() {
    PageIndicatorDot(
        color = Color.Black,
    )
}
