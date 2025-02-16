package com.lukeneedham.videodiary.ui.feature.exportdiary.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
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

    val controller = remember {
        VideoPlayerController().apply {
            isVolumeOn = true
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val isPortrait = height > width

        @Composable
        fun TextInfo(
            modifier: Modifier = Modifier
        ) {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val start = exportedVideo.startDate.format(StandardDateTimeFormatter.date)
                val end = exportedVideo.endDate.format(StandardDateTimeFormatter.date)
                Text(
                    "Video Diary Export",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = Typography.Size.big,
                )
                Text(
                    "$start to $end",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = Typography.Size.extraSmall,
                )
                Text(
                    "${exportedVideo.dayVideoCount} videos",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = Typography.Size.extraSmall,
                )
            }
        }

        @Composable
        fun Video(
            modifier: Modifier = Modifier
        ) {
            if (videoAspectRatio != null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                ) {
                    VideoPlayer(
                        video = Video.PersistedFile(videoFile),
                        aspectRatio = videoAspectRatio,
                        controller = controller,
                    )
                }
            }
        }

        @Composable
        fun Controls(
            modifier: Modifier = Modifier
        ) {
            Column(
                modifier = modifier
            ) {
                VideoControlActionBar(
                    hasVideo = true,
                    videoPlayerController = controller,
                )
                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                        .width(10.dp)
                )
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

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            GenericToolbar(
                canGoBack = canGoBack, onBack = onBack,
            )

            if (isPortrait) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(15.dp)
                ) {
                    TextInfo(
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Video(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Controls(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(15.dp)
                ) {
                    TextInfo()
                    Spacer(modifier = Modifier.width(10.dp))
                    Video(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Controls(
                        modifier = Modifier.width(IntrinsicSize.Min)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewPortrait() {
    Box(
        modifier = Modifier
            .height(3000.dp)
            .width(1000.dp)
    ) {
        ExportDiaryViewPageContent(
            exportedVideo = MockDataExportDiaryView.exportedVideo,
            share = {},
            videoAspectRatio = 1f,
            canGoBack = true,
            onBack = {},
        )
    }
}

@Preview
@Composable
private fun PreviewLandscape() {
    Box(
        modifier = Modifier
            .width(3000.dp)
            .height(1000.dp)
    ) {
        ExportDiaryViewPageContent(
            exportedVideo = MockDataExportDiaryView.exportedVideo,
            share = {},
            videoAspectRatio = 1f,
            canGoBack = true,
            onBack = {},
        )
    }
}