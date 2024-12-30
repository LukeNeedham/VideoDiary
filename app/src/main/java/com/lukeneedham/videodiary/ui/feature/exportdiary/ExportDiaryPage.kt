package com.lukeneedham.videodiary.ui.feature.exportdiary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController

@Composable
fun ExportDiaryPage(
    viewModel: ExportDiaryViewModel,
) {
    val videoCount = viewModel.videoCount
    val videoAspectRatio = viewModel.videoAspectRatio

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (videoCount != null) {
            val text = "There are $videoCount videos in your diary"
            Text(
                text = text
            )
        }

        val state = viewModel.exportState
        when (state) {
            ExportState.InProgress -> {
                CircularProgressIndicator()
            }

            ExportState.NoVideos -> {
                Text(
                    text = "There are no videos to export!"
                )
            }

            ExportState.Ready -> {
                Button(
                    text = "Export",
                    onClick = {
                        viewModel.export()
                    }
                )
            }

            is ExportState.Success -> {
                Text(
                    text = "Successfully completed!"
                )
                if (videoAspectRatio != null) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color.Black)
                    ) {
                        VideoPlayer(
                            video = Video.PersistedFile(state.exportedFile),
                            aspectRatio = videoAspectRatio,
                            controller = rememberVideoPlayerController(),
                        )
                    }
                }
            }
        }

    }
}