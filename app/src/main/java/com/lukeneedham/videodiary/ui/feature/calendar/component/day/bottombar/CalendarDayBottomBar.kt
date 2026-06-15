package com.lukeneedham.videodiary.ui.feature.calendar.component.day.bottombar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController

@Composable
fun CalendarDayBottomBar(
    videoPlayerController: VideoPlayerController,
    day: Day,
    isEditable: Boolean,
    onRecordVideoClick: () -> Unit,
    onDeleteVideoClick: () -> Unit,
    share: (ShareRequest) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasVideo = day.videoFile != null
    val date = day.date
    val video = day.videoFile

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (hasVideo) {
            // Mute button
            val muteButtonIcon =
                if (videoPlayerController.isVolumeOn) R.drawable.volume_on else R.drawable.volume_off
            val muteButtonText = if (videoPlayerController.isVolumeOn) "Mute" else "Unmute"
            GlassIconButton(
                iconRes = muteButtonIcon,
                contentDescription = muteButtonText,
                onClick = {
                    videoPlayerController.toggleVolumeOn()
                }
            )

            // Play button
            val isPlaying = !videoPlayerController.isTogglePaused
            val playButtonIcon = if (isPlaying) R.drawable.pause else R.drawable.play
            val playButtonText = if (isPlaying) "Pause" else "Play"
            GlassIconButton(
                iconRes = playButtonIcon,
                contentDescription = playButtonText,
                onClick = {
                    videoPlayerController.toggleIsPlaying()
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isEditable) {
                GlassIconButton(
                    iconRes = R.drawable.retake,
                    contentDescription = "Retake",
                    onClick = { onRecordVideoClick() },
                )
            }

            GlassIconButton(
                iconRes = R.drawable.share,
                contentDescription = "Share",
                onClick = {
                    val dateText = date.format(StandardDateTimeFormatter.date)
                    val text = "Video Diary: $dateText"
                    val request = ShareRequest(
                        title = text,
                        text = text,
                        video = video,
                    )
                    share(request)
                },
            )

            if (isEditable) {
                GlassIconButton(
                    iconRes = R.drawable.delete,
                    contentDescription = "Delete",
                    onClick = onDeleteVideoClick,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CalendarDayBottomBar(
        videoPlayerController = rememberVideoPlayerController(),
        day = MockDataCalendar.dayWithVideo,
        isEditable = false,
        onRecordVideoClick = {},
        onDeleteVideoClick = {},
        share = {},
    )
}
