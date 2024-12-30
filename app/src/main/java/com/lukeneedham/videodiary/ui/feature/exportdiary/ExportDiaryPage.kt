package com.lukeneedham.videodiary.ui.feature.exportdiary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController

@Composable
fun ExportDiaryPage(
    viewModel: ExportDiaryViewModel,
    share: (ShareRequest) -> Unit,
) {
    val videoCount = viewModel.videoCount
    val videoAspectRatio = viewModel.videoAspectRatio

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        if (videoCount != null) {
            val text = "There are $videoCount videos in your diary"
            Text(
                text = text
            )
            Spacer(modifier = Modifier.height(10.dp))
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
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            is ExportState.Success -> {
                val videoFile = state.exportedFile

                Text(
                    text = "Successfully completed!"
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (videoAspectRatio != null) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        VideoPlayer(
                            video = Video.PersistedFile(videoFile),
                            aspectRatio = videoAspectRatio,
                            controller = rememberVideoPlayerController(),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    text = "Share",
                    onClick = {
                        val request = ShareRequest(
                            title = "Full diary video",
                            text = "This is my complete video diary",
                            video = videoFile,
                        )
                        share(request)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}