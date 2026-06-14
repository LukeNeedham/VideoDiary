package com.lukeneedham.videodiary.ui.feature.calendar.component.portrait

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.BuildConfig
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun CalendarToolbarPortrait(
    exportFullVideo: () -> Unit,
    onDebugClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Video Diary",
            color = Color.White,
            fontSize = Typography.Size.medium,
            modifier = Modifier.weight(1f)
        )
        if (BuildConfig.DEBUG) {
            GlassIconButton(
                iconRes = R.drawable.bug,
                contentDescription = "Debug options",
                onClick = onDebugClick,
            )
        }
        GlassIconButton(
            iconRes = R.drawable.movie,
            contentDescription = "Export full video",
            onClick = exportFullVideo,
        )
    }
}

@Preview
@Composable
internal fun PreviewToolbarPortrait() {
    CalendarToolbarPortrait(
        exportFullVideo = {},
        onDebugClick = {},
    )
}
