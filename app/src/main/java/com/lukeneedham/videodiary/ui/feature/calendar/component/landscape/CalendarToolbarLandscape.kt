package com.lukeneedham.videodiary.ui.feature.calendar.component.landscape

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.toolbar.ToolbarSize

@Composable
fun CalendarToolbarLandscape(
    exportFullVideo: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(ToolbarSize.landscapeToolbarWidth)
            .background(color = Color.Black)
            .padding(vertical = 10.dp)
            .padding(start = 5.dp, end = 15.dp)
    ) {
        Text(
            text = "Video Diary",
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(1f))
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
    CalendarToolbarLandscape(
        exportFullVideo = {},
    )
}