package com.lukeneedham.videodiary.ui.feature.common.glass

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.theme.AppTheme
import com.lukeneedham.videodiary.ui.theme.GlassBorder
import com.lukeneedham.videodiary.ui.theme.GlassFill

/**
 * A translucent "glass" panel, intended to float over full-bleed video/camera content.
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    fillColor: Color = GlassFill,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(fillColor, shape)
            .border(width = 1.dp, color = GlassBorder, shape = shape),
        content = content,
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(Color.DarkGray)
                .padding(20.dp),
        ) {
            GlassSurface(modifier = Modifier.size(width = 160.dp, height = 80.dp)) {
                Text(
                    text = "Glass",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}
