package com.lukeneedham.videodiary.ui.feature.common.glass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.theme.AppTheme

/**
 * A full-width row of "glass" controls (e.g. mute/play/share buttons), intended for a video's
 * bottom toolbar.
 */
@Composable
fun VideoControlsRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
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
            VideoControlsRow {
                GlassIconButton(
                    iconRes = R.drawable.close,
                    contentDescription = "Close",
                    onClick = {},
                )
                GlassButton(
                    text = "Share",
                    onClick = {},
                )
            }
        }
    }
}
