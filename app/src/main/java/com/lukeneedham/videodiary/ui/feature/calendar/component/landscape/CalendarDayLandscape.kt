package com.lukeneedham.videodiary.ui.feature.calendar.component.landscape

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
fun CalendarDayLandscape(
    day: Day,
    videoAspectRatio: Float,
    videoPlayerController: VideoPlayerController,
    onRecordTodayVideoClick: () -> Unit,
    onDeleteTodayVideoClick: () -> Unit,
    share: (ShareRequest) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
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
        }
        Spacer(modifier = Modifier.width(5.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                // Fixed width, so bar will always render regardless of whether any buttons render
                .width(120.dp)
                .fillMaxHeight()
        ) {
            VideoControlActionBar(
                hasVideo = day.video != null,
                videoPlayerController = videoPlayerController,
            )

            CalendarDayActionBarContent(
                day = day,
                onRecordTodayVideoClick = onRecordTodayVideoClick,
                onDeleteTodayVideoClick = onDeleteTodayVideoClick,
                share = share,
                buttonModifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayLandscape() {
    CalendarDayLandscape(
        day = MockDataCalendar.day,
        onRecordTodayVideoClick = {},
        videoAspectRatio = 1f,
        videoPlayerController = rememberVideoPlayerController(),
        onDeleteTodayVideoClick = {},
        share = {},
    )
}