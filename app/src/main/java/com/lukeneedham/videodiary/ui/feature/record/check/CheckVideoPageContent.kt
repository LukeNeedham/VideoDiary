package com.lukeneedham.videodiary.ui.feature.record.check

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassAcceptButton
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton
import com.lukeneedham.videodiary.ui.feature.common.glass.TopScrim
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController

@Composable
fun CheckVideoPageContent(
    video: Video,
    videoAspectRatio: Float?,
    onCancelClick: () -> Unit,
    onRetakeClick: () -> Unit,
    onAccepted: () -> Unit,
) {
    val videoPlayerController = remember {
        VideoPlayerController().apply {
            playingVideo = video
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (videoAspectRatio != null) {
                VideoPlayer(
                    video = video,
                    aspectRatio = videoAspectRatio,
                    controller = videoPlayerController,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            TopScrim(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .height(140.dp)
            )

            GlassIconButton(
                iconRes = R.drawable.close,
                contentDescription = "Cancel",
                onClick = onCancelClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .safeDrawingPadding()
                    .padding(16.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .navigationBarsPadding()
                .height(120.dp)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                val muteIcon =
                    if (videoPlayerController.isVolumeOn) R.drawable.volume_on else R.drawable.volume_off
                GlassIconButton(
                    iconRes = muteIcon,
                    contentDescription = "Toggle sound",
                    onClick = { videoPlayerController.toggleVolumeOn() },
                )

                val isPlaying = !videoPlayerController.isTogglePaused
                val playIcon = if (isPlaying) R.drawable.pause else R.drawable.play
                GlassIconButton(
                    iconRes = playIcon,
                    contentDescription = "Play/pause",
                    onClick = { videoPlayerController.toggleIsPlaying() },
                )
            }

            GlassAcceptButton(
                onClick = onAccepted,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 10.dp)
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )

            GlassIconButton(
                iconRes = R.drawable.retake,
                contentDescription = "Retake",
                onClick = onRetakeClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewCheckVideoPageContent() {
    CheckVideoPageContent(
        video = MockDataCheckVideo.video,
        videoAspectRatio = 1f,
        onRetakeClick = {},
        onAccepted = {},
        onCancelClick = {},
    )
}
