package com.lukeneedham.videodiary.ui.feature.calendar.component.portrait

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.calendar.component.day.actionbar.CalendarDayActionBarContent
import com.lukeneedham.videodiary.ui.feature.calendar.component.day.card.CalendarDayCard
import com.lukeneedham.videodiary.ui.feature.common.glass.BottomScrim
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoControlActionBar
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import java.time.LocalDate

@Composable
fun CalendarDayPortrait(
    day: Day,
    videoAspectRatio: Float,
    videoPlayerController: VideoPlayerController,
    allowRetakeForPastDays: Boolean,
    onRecordVideoClick: (date: LocalDate) -> Unit,
    onDeleteTodayVideoClick: () -> Unit,
    share: (ShareRequest) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CalendarDayCard(
            day = day,
            videoAspectRatio = videoAspectRatio,
            allowRetakeForPastDays = allowRetakeForPastDays,
            onRecordVideoClick = onRecordVideoClick,
            videoPlayerController = videoPlayerController,
            modifier = Modifier.fillMaxSize(),
        )

        BottomScrim(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(190.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .safeDrawingPadding()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            VideoControlActionBar(
                hasVideo = day.video != null,
                videoPlayerController = videoPlayerController,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    // Fixed height, so bar will always render regardless of whether any buttons render
                    .height(56.dp)
                    .fillMaxWidth()
            ) {
                CalendarDayActionBarContent(
                    day = day,
                    allowRetakeForPastDays = allowRetakeForPastDays,
                    onRecordVideoClick = onRecordVideoClick,
                    onDeleteTodayVideoClick = onDeleteTodayVideoClick,
                    share = share,
                    buttonModifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayPortrait() {
    CalendarDayPortrait(
        day = MockDataCalendar.dayWithVideo,
        allowRetakeForPastDays = false,
        onRecordVideoClick = {},
        videoAspectRatio = 1f,
        videoPlayerController = rememberVideoPlayerController(),
        onDeleteTodayVideoClick = {},
        share = {},
    )
}
