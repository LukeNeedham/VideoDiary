package com.lukeneedham.videodiary.ui.feature.exportdiary.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoControlActionBar
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun ExportDiaryViewPageContent(
    exportedVideo: ExportedVideo,
    share: (ShareRequest) -> Unit,
    videoAspectRatio: Float?,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    val videoFile = exportedVideo.videoFile

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GenericToolbar(
            canGoBack = canGoBack, onBack = onBack,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(15.dp)
        ) {
            val start = exportedVideo.startDate.format(StandardDateTimeFormatter.date)
            val end = exportedVideo.endDate.format(StandardDateTimeFormatter.date)
            Text(
                "Video Diary Export",
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = Typography.Size.big,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "$start to $end",
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = Typography.Size.extraSmall,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "${exportedVideo.dayVideoCount} videos",
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = Typography.Size.extraSmall,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (videoAspectRatio != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val controller = remember {
                        VideoPlayerController().apply {
                            isVolumeOn = true
                        }
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        VideoPlayer(
                            video = Video.PersistedFile(videoFile),
                            aspectRatio = videoAspectRatio,
                            controller = controller,
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    VideoControlActionBar(
                        hasVideo = true,
                        videoPlayerController = controller,
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                text = "Share",
                onClick = {
                    val shareText = "Full Video Diary"
                    val request = ShareRequest(
                        title = shareText,
                        text = shareText,
                        video = videoFile,
                    )
                    share(request)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewExportDiaryViewPageContent() {
    ExportDiaryViewPageContent(
        exportedVideo = MockDataExportDiaryView.exportedVideo,
        share = {},
        videoAspectRatio = 1f,
        canGoBack = true,
        onBack = {},
    )
}