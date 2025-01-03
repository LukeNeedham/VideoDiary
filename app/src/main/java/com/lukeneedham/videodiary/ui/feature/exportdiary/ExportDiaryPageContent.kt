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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController

@Composable
fun ExportDiaryPageContent(
    share: (ShareRequest) -> Unit,
    canGoBack: Boolean,
    onBack: () -> Unit,
    videoCount: Int?,
    exportState: ExportState,
    export: () -> Unit,
    videoAspectRatio: Float?,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        GenericToolbar(
            canGoBack = canGoBack, onBack = onBack,
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(10.dp)
        ) {

            if (videoCount != null) {
                val text = "There are $videoCount videos in your diary"
                Text(
                    text = text
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            when (exportState) {
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
                            export()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is ExportState.Success -> {
                    val videoFile = exportState.exportedFile

                    if (videoAspectRatio != null) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            val controller = remember {
                                VideoPlayerController().apply {
                                    isVolumeOn = true
                                }
                            }
                            VideoPlayer(
                                video = Video.PersistedFile(videoFile),
                                aspectRatio = videoAspectRatio,
                                controller = controller,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        text = "Share",
                        onClick = {
                            val text = "Full Video Diary"
                            val request = ShareRequest(
                                title = text,
                                text = text,
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
}

@Preview
@Composable
internal fun PreviewExportDiaryPageContent() {
    ExportDiaryPageContent(
        share = {},
        canGoBack = true,
        onBack = {},
        videoCount = 1,
        exportState = MockDataExportDiary.exportState,
        export = {},
        videoAspectRatio = 1f

    )
}