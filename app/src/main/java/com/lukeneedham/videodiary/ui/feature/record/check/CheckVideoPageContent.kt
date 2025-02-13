package com.lukeneedham.videodiary.ui.feature.record.check

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.record.check.component.CheckVideoActionBar

@Composable
fun CheckVideoPageContent(
    video: Video,
    videoAspectRatio: Float?,
    onCancelClick: () -> Unit,
    onRetakeClick: () -> Unit,
    onAccepted: () -> Unit,
) {
    val videoPlayerController = remember { VideoPlayerController() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (videoAspectRatio != null) {
                VideoPlayer(
                    video = video,
                    aspectRatio = videoAspectRatio,
                    controller = videoPlayerController,
                )
            }
        }
        CheckVideoActionBar(
            videoPlayerController = videoPlayerController,
            onCancelClick = onCancelClick,
            onRetakeClick = onRetakeClick,
            onAccepted = onAccepted,
            modifier = Modifier
                .fillMaxWidth()
        )
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