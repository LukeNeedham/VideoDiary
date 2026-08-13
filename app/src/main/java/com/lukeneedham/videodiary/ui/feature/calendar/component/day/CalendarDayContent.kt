package com.lukeneedham.videodiary.ui.feature.calendar.component.day

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.calendar.component.day.card.CalendarDayCard
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController

@Composable
fun CalendarDayContent(
    day: Day,
    videoAspectRatio: Float,
    videoPlayerController: VideoPlayerController,
    allowEditPastDays: Boolean,
    onRecordVideoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CalendarDayCard(
        day = day,
        videoAspectRatio = videoAspectRatio,
        allowRetakeForPastDays = allowEditPastDays,
        onRecordVideoClick = onRecordVideoClick,
        videoPlayerController = videoPlayerController,
        modifier = modifier.fillMaxSize(),
    )
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
    )
}
