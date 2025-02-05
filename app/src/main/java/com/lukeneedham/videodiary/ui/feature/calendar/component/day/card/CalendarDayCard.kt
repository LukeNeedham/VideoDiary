package com.lukeneedham.videodiary.ui.feature.calendar.component.day.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController

@Composable
fun CalendarDayCard(
    day: Day,
    videoAspectRatio: Float,
    videoPlayerController: VideoPlayerController,
    onRecordTodayVideoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        val video = day.video
        val isToday = day.isToday
        if (video == null) {
            if (isToday) {
                CalendarDayCardTodayMissing(
                    modifier = Modifier.clickable {
                        onRecordTodayVideoClick()
                    }
                )
            } else {
                CalendarDayCardPastMissing()
            }
        } else {
            CalendarDayCardVideo(
                video = video,
                videoAspectRatio = videoAspectRatio,
                videoPlayerController = videoPlayerController,
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayCard() {
    CalendarDayCard(
        day = MockDataCalendar.dayWithVideo,
        videoAspectRatio = 1f,
        onRecordTodayVideoClick = {},
        videoPlayerController = rememberVideoPlayerController(),
    )
}