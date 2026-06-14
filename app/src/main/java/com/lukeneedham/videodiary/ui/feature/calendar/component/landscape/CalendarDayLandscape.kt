package com.lukeneedham.videodiary.ui.feature.calendar.component.landscape

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.lukeneedham.videodiary.ui.feature.common.glass.EndScrim
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoControlActionBar
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import java.time.LocalDate

@Composable
fun CalendarDayLandscape(
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

        EndScrim(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(220.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(140.dp)
                .fillMaxHeight()
                .safeDrawingPadding()
                .padding(vertical = 16.dp, horizontal = 10.dp)
        ) {
            VideoControlActionBar(
                hasVideo = day.video != null,
                videoPlayerController = videoPlayerController,
            )

            CalendarDayActionBarContent(
                day = day,
                allowRetakeForPastDays = allowRetakeForPastDays,
                onRecordVideoClick = onRecordVideoClick,
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
        day = MockDataCalendar.dayWithVideo,
        allowRetakeForPastDays = false,
        onRecordVideoClick = {},
        videoAspectRatio = 1f,
        videoPlayerController = rememberVideoPlayerController(),
        onDeleteTodayVideoClick = {},
        share = {},
    )
}
