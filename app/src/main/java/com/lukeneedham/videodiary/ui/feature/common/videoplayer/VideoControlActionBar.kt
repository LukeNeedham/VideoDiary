package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton

@Composable
fun VideoControlActionBar(
    hasVideo: Boolean,
    videoPlayerController: VideoPlayerController,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            // Fixed height, so bar will always render regardless of whether any buttons render
            .height(52.dp)
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
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayVideoControlBar() {
    VideoControlActionBar(
        hasVideo = true,
        videoPlayerController = rememberVideoPlayerController(),
    )
}
