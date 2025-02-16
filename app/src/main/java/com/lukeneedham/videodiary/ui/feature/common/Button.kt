package com.lukeneedham.videodiary.ui.feature.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = Color.Black,
    foregroundColor: Color = Color.White,
) {
    val backgroundAlpha = if (enabled) 1f else 0.5f
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .heightIn(min = 50.dp)
            .widthIn(min = 50.dp)
            .background(
                color = backgroundColor.copy(alpha = backgroundAlpha),
                shape = RoundedCornerShape(10.dp),
            )
            .clickable(enabled = enabled) {
                onClick()
            }
            .padding(10.dp)
    ) {
        Text(
            text = text,
            color = foregroundColor,
        )
    }
}

@Preview
@Composable
internal fun PreviewButton() {
    Button(
        text = "Test",
        enabled = true,
        onClick = {}
    )
}