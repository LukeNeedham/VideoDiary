package com.lukeneedham.videodiary.ui.feature.calendar.component.landscape

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R

@Composable
fun ToolbarLandscape(
    exportFullVideo: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = Color.Black)
            .padding(vertical = 10.dp)
            .padding(start = 5.dp, end = 15.dp)
    ) {
        Text(
            text = "Video Diary",
            color = Color.White,
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { exportFullVideo() }
        ) {
            Image(
                painter = painterResource(R.drawable.movie),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.White),
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Export",
                color = Color.White,
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewToolbarLandscape() {
    ToolbarLandscape(
        exportFullVideo = {},
    )
}