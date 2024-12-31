package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.calendar.component.day.actionbar.CalendarDayActionBar
import com.lukeneedham.videodiary.ui.feature.calendar.component.day.actionbar.CalendarDayVideoControlBar
import com.lukeneedham.videodiary.ui.feature.calendar.component.day.card.CalendarDayCard
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController

@Composable
fun CalendarDay(
    day: Day,
    videoAspectRatio: Float,
    videoPlayerController: VideoPlayerController,
    onRecordTodayVideoClick: () -> Unit,
    onDeleteTodayVideoClick: () -> Unit,
    share: (ShareRequest) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        CalendarDayCard(
            day = day,
            videoAspectRatio = videoAspectRatio,
            onRecordTodayVideoClick = onRecordTodayVideoClick,
            videoPlayerController = videoPlayerController,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        CalendarDayVideoControlBar(
            hasVideo = day.video != null,
            videoPlayerController = videoPlayerController,
        )
        Spacer(modifier = Modifier.height(10.dp))
        CalendarDayActionBar(
            day = day,
            onRecordTodayVideoClick = onRecordTodayVideoClick,
            onDeleteTodayVideoClick = onDeleteTodayVideoClick,
            share = share
        )
    }
}

@Preview
@Composable
internal fun PreviewCalendarDay() {
    CalendarDay(
        day = MockDataCalendar.day,
        onRecordTodayVideoClick = {},
        videoAspectRatio = 1f,
        videoPlayerController = rememberVideoPlayerController(),
        onDeleteTodayVideoClick = {},
        share = {},
    )
}