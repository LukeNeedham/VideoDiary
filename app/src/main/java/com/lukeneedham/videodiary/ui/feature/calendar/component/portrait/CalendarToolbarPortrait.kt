package com.lukeneedham.videodiary.ui.feature.calendar.component.portrait

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.lukeneedham.videodiary.ui.feature.common.toolbar.ToolbarSize

@Composable
fun CalendarToolbarPortrait(
    exportFullVideo: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(ToolbarSize.portraitToolbarHeight)
            .background(color = Color.Black)
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Text(
            text = "Video Diary",
            color = Color.White,
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.movie),
            contentDescription = "Export full video",
            colorFilter = ColorFilter.tint(color = Color.White),
            modifier = Modifier
                .size(50.dp)
                .clickable {
                    exportFullVideo()
                }
                .padding(10.dp)
        )
    }
}

@Preview
@Composable
internal fun PreviewToolbarPortrait() {
    CalendarToolbarPortrait(
        exportFullVideo = {},
    )
}