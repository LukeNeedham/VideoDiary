package com.lukeneedham.videodiary.ui.feature.common.glass

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.theme.AccentRecord
import com.lukeneedham.videodiary.ui.theme.GlassBorder
import com.lukeneedham.videodiary.ui.theme.GlassFill

/**
 * A translucent "glass" panel, intended to float over full-bleed video/camera content.
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(GlassFill, shape)
            .border(width = 1.dp, color = GlassBorder, shape = shape),
        content = content,
    )
}

@Composable
fun GlassIconButton(
    iconRes: Int,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
    enabled: Boolean = true,
    size: Dp = 50.dp,
    iconSize: Dp = 24.dp,
) {
    GlassSurface(
        shape = CircleShape,
        modifier = modifier
            .size(size)
            .alpha(if (enabled) 1f else 0.4f)
            .clickable(enabled = enabled, onClick = onClick),
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(tint),
            modifier = Modifier
                .align(Alignment.Center)
                .size(iconSize),
        )
    }
}

@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = Color.White,
) {
    GlassSurface(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .heightIn(min = 48.dp)
            .alpha(if (enabled) 1f else 0.4f)
            .clickable(enabled = enabled, onClick = onClick),
    ) {
        Text(
            text = text,
            color = contentColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 22.dp, vertical = 12.dp),
        )
    }
}

/**
 * A large camera-style record button: a glass ring whose centre morphs between a circle (idle)
 * and a rounded square (recording), matching the standard camera-app affordance.
 */
@Composable
fun GlassRecordButton(
    isRecording: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(76.dp)
            .alpha(if (enabled) 1f else 0.4f)
            .clip(CircleShape)
            .background(GlassFill, CircleShape)
            .border(width = 3.dp, color = Color.White, shape = CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
    ) {
        val innerShape = if (isRecording) RoundedCornerShape(8.dp) else CircleShape
        val innerSize = if (isRecording) 28.dp else 60.dp
        Box(
            modifier = Modifier
                .size(innerSize)
                .background(AccentRecord, innerShape)
        )
    }
}

/** Gradient scrim fading to black at the top edge, keeping overlay controls legible over bright video. */
@Composable
fun TopScrim(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent),
                )
            )
    )
}

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
private fun PreviewGlassComponents() {
    Box(
        modifier = Modifier
            .background(Color.DarkGray)
            .padding(20.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlassIconButton(
                iconRes = R.drawable.close,
                contentDescription = "Close",
                onClick = {},
            )
            GlassButton(
                text = "Retake",
                onClick = {},
            )
            GlassRecordButton(
                isRecording = false,
                onClick = {},
            )
            GlassRecordButton(
                isRecording = true,
                onClick = {},
            )
        }
    }
}
