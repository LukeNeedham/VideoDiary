package com.lukeneedham.videodiary.ui.feature.calendar.component.day.actionbar

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
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import java.time.format.DateTimeFormatter

@Composable
fun CalendarDayActionBar(
    day: Day,
    videoPlayerController: VideoPlayerController,
    onRecordTodayVideoClick: () -> Unit,
    onDeleteTodayVideoClick: () -> Unit,
    share: (ShareRequest) -> Unit,
    modifier: Modifier = Modifier
) {
    val date = day.date
    val video = day.video
    val isToday = day.isToday

    Column(
        modifier = modifier
    ) {
        // Row 1 - for video playback controls
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (video != null) {
                // Mute button
                val muteButtonText = if (videoPlayerController.isVolumeOn) "Mute" else "Unmute"
                Button(
                    text = muteButtonText,
                    onClick = {
                        videoPlayerController.toggleVolumeOn()
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Play button
                val playButtonText = if (videoPlayerController.isPlaying) "Pause" else "Play"
                Button(
                    text = playButtonText,
                    onClick = {
                        videoPlayerController.toggleIsPlaying()
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Row 2 - for video editing
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (video == null) {
                if (isToday) {
                    Button(
                        text = "Record",
                        onClick = onRecordTodayVideoClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                }
            } else {
                if (isToday) {
                    Button(
                        text = "Retake",
                        onClick = onRecordTodayVideoClick,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )

                    Button(
                        text = "Delete",
                        onClick = onDeleteTodayVideoClick,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }

                Button(
                    text = "Share",
                    onClick = {
                        val dateText = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                        val text = "Video Diary: $dateText"
                        val request = ShareRequest(
                            title = text,
                            text = text,
                            video = video,
                        )
                        share(request)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayActionBar() {
    CalendarDayActionBar(
        day = MockDataCalendar.day,
        onRecordTodayVideoClick = {},
        videoPlayerController = rememberVideoPlayerController(),
        onDeleteTodayVideoClick = {},
        share = {},
    )
}