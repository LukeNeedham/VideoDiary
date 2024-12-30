package com.lukeneedham.videodiary.ui.feature.checkvideo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController

@Composable
fun CheckVideoPageContent(
    video: Video,
    videoAspectRatio: Float?,
    onRetakeClick: () -> Unit,
    onAccepted: () -> Unit,
) {
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
                    controller = rememberVideoPlayerController(),
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 10.dp, top = 5.dp)
        ) {
            @Composable
            fun Button(
                text: String,
                onClick: () -> Unit,
            ) {
                Button(
                    text = text,
                    onClick = onClick,
                    backgroundColor = Color.White,
                    foregroundColor = Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                text = "Retake",
                onClick = onRetakeClick,
            )

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                text = "Accept!",
                onClick = onAccepted,
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
    )
}