package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R

@Composable
fun VideoControlActionBar(
    hasVideo: Boolean,
    videoPlayerController: VideoPlayerController,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            space = 10.dp,
            alignment = Alignment.CenterHorizontally
        ),
        modifier = Modifier
            // Fixed height, so bar will always render regardless of whether any buttons render
            .height(50.dp)
            .fillMaxWidth()
    ) {
        @Composable
        fun Button(
            iconRes: Int,
            contentDescription: String,
            onClick: () -> Unit,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        color = Color.Gray,
                    )
                    .clickable {
                        onClick()
                    }
                    .padding(10.dp)
            ) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = contentDescription,
                    colorFilter = ColorFilter.tint(color = Color.White),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        if (hasVideo) {
            // Mute button
            val muteButtonIcon =
                if (videoPlayerController.isVolumeOn) R.drawable.volume_on else R.drawable.volume_off
            val muteButtonText = if (videoPlayerController.isVolumeOn) "Mute" else "Unmute"
            Button(
                iconRes = muteButtonIcon,
                contentDescription = muteButtonText,
                onClick = {
                    videoPlayerController.toggleVolumeOn()
                }
            )

            // Play button
            val playButtonIcon =
                if (videoPlayerController.isPlaying) R.drawable.pause else R.drawable.play
            val playButtonText = if (videoPlayerController.isPlaying) "Pause" else "Play"
            Button(
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