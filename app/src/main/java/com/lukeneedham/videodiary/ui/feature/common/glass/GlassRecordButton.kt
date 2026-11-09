package com.lukeneedham.videodiary.ui.feature.common.glass

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.theme.AccentRecord
import com.lukeneedham.videodiary.ui.theme.AppTheme
import com.lukeneedham.videodiary.ui.theme.GlassFill

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
            .alpha(if (enabled) 1f else 0.4f)
            .clip(CircleShape)
            .background(GlassFill, CircleShape)
            .border(width = 3.dp, color = Color.White, shape = CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
    ) {
        val innerShape = if (isRecording) RoundedCornerShape(8.dp) else CircleShape
        val innerFraction = if (isRecording) 0.37f else 0.79f
        Box(
            modifier = Modifier
                .fillMaxSize(innerFraction)
                .background(AccentRecord, innerShape)
        )
    }
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
            GlassRecordButton(
                isRecording = false,
                onClick = {},
                modifier = Modifier.size(76.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRecording() {
    AppTheme {
        Box(
            modifier = Modifier
                .background(Color.DarkGray)
                .padding(20.dp),
        ) {
            GlassRecordButton(
                isRecording = true,
                onClick = {},
                modifier = Modifier.size(76.dp),
            )
        }
    }
}
