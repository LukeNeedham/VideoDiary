package com.lukeneedham.videodiary.ui.feature.calendar.component.portrait

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.calendar.component.day.actionbar.CalendarDayActionBarContent
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoControlActionBar
import com.lukeneedham.videodiary.ui.feature.calendar.component.day.card.CalendarDayCard
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController

@Composable
fun CalendarDayPortrait(
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
        VideoControlActionBar(
            hasVideo = day.video != null,
            videoPlayerController = videoPlayerController,
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                // Fixed height, so bar will always render regardless of whether any buttons render
                .height(60.dp)
                .fillMaxWidth()
        ) {
            CalendarDayActionBarContent(
                day = day,
                onRecordTodayVideoClick = onRecordTodayVideoClick,
                onDeleteTodayVideoClick = onDeleteTodayVideoClick,
                share = share,
                buttonModifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayPortrait() {
    CalendarDayPortrait(
        day = MockDataCalendar.dayWithVideo,
        onRecordTodayVideoClick = {},
        videoAspectRatio = 1f,
        videoPlayerController = rememberVideoPlayerController(),
        onDeleteTodayVideoClick = {},
        share = {},
    )
}