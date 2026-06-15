package com.lukeneedham.videodiary.ui.feature.calendar.component.day

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.calendar.component.day.bottombar.CalendarDayBottomBar
import com.lukeneedham.videodiary.ui.feature.calendar.component.day.card.CalendarDayCard
import com.lukeneedham.videodiary.ui.feature.common.glass.BottomScrim
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController

@Composable
fun CalendarDayContent(
    day: Day,
    videoAspectRatio: Float,
    videoPlayerController: VideoPlayerController,
    allowEditPastDays: Boolean,
    onRecordVideoClick: () -> Unit,
    onDeleteVideoClick: () -> Unit,
    share: (ShareRequest) -> Unit,
    modifier: Modifier = Modifier
) {
    val isEditable = day.isToday || allowEditPastDays

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CalendarDayCard(
            day = day,
            videoAspectRatio = videoAspectRatio,
            allowRetakeForPastDays = allowEditPastDays,
            onRecordVideoClick = onRecordVideoClick,
            videoPlayerController = videoPlayerController,
            modifier = Modifier.fillMaxSize(),
        )

        BottomScrim(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(190.dp)
        )

        CalendarDayBottomBar(
            videoPlayerController = videoPlayerController,
            day = day,
            isEditable = isEditable,
            onRecordVideoClick = onRecordVideoClick,
            onDeleteVideoClick = onDeleteVideoClick,
            share = share,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 10.dp)
        )
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayContent() {
    CalendarDayContent(
        day = MockDataCalendar.dayWithVideo,
        allowEditPastDays = false,
        onRecordVideoClick = {},
        videoAspectRatio = 1f,
        videoPlayerController = rememberVideoPlayerController(),
        onDeleteVideoClick = {},
        share = {},
    )
}
