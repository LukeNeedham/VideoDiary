package com.lukeneedham.videodiary.ui.feature.common.glass

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.theme.AccentAccept
import com.lukeneedham.videodiary.ui.theme.AppTheme
import com.lukeneedham.videodiary.ui.theme.GlassFill

@Composable
fun GlassAcceptButton(
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
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize(0.79f)
                .background(AccentAccept, CircleShape)
        ) {
            Image(
                painter = painterResource(R.drawable.tick),
                contentDescription = "Accept",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.fillMaxSize(0.53f),
            )
        }
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
            GlassAcceptButton(
                onClick = {},
                modifier = Modifier.size(76.dp),
            )
        }
    }
}
