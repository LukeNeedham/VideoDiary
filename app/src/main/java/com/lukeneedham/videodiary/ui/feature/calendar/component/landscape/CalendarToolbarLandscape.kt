package com.lukeneedham.videodiary.ui.feature.calendar.component.landscape

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.BuildConfig
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassSurface
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun CalendarToolbarLandscape(
    exportFullVideo: () -> Unit,
    onDebugClick: () -> Unit,
) {
    GlassSurface {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Video Diary",
                color = Color.White,
                fontSize = Typography.Size.medium,
            )
            if (BuildConfig.DEBUG) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDebugClick() }
                ) {
                    Image(
                        painter = painterResource(R.drawable.bug),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = Color.White),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Debug",
                        color = Color.White,
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { exportFullVideo() }
            ) {
                Image(
                    painter = painterResource(R.drawable.movie),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = Color.White),
                    modifier = Modifier.size(24.dp)
                )
                Row(modifier = Modifier.size(10.dp)) {}
                Text(
                    text = "Export",
                    color = Color.White,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewToolbarLandscape() {
    CalendarToolbarLandscape(
        exportFullVideo = {},
        onDebugClick = {},
    )
}
