package com.lukeneedham.videodiary.ui.feature.exportdiary.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.common.glass.BottomScrim
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassButton
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassSurface
import com.lukeneedham.videodiary.ui.feature.common.glass.TopScrim
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (videoAspectRatio != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                VideoPlayer(
                    video = Video.PersistedFile(videoFile),
                    aspectRatio = videoAspectRatio,
                    controller = controller,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        TopScrim(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .height(140.dp)
        )
        BottomScrim(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(160.dp)
        )

        if (canGoBack) {
            GlassIconButton(
                iconRes = R.drawable.back,
                contentDescription = "Back",
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .safeDrawingPadding()
                    .padding(16.dp)
            )
        }

        val start = exportedVideo.startDate.format(StandardDateTimeFormatter.date)
        val end = exportedVideo.endDate.format(StandardDateTimeFormatter.date)
        GlassSurface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .safeDrawingPadding()
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            ) {
                Text(
                    text = "Video Diary Export",
                    color = Color.White,
                    textAlign = TextAlign.End,
                    fontSize = Typography.Size.medium,
                )
                Text(
                    text = "$start to $end",
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.End,
                    fontSize = Typography.Size.extraSmall,
                )
                Text(
                    text = "${exportedVideo.dayVideoCount} videos",
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.End,
                    fontSize = Typography.Size.extraSmall,
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .safeDrawingPadding()
                .padding(bottom = 24.dp)
        ) {
            val muteIcon =
                if (controller.isVolumeOn) R.drawable.volume_on else R.drawable.volume_off
            GlassIconButton(
                iconRes = muteIcon,
                contentDescription = "Toggle sound",
                onClick = { controller.toggleVolumeOn() },
            )

            val isPlaying = !controller.isTogglePaused
            val playIcon = if (isPlaying) R.drawable.pause else R.drawable.play
            GlassIconButton(
                iconRes = playIcon,
                contentDescription = "Play/pause",
                onClick = { controller.toggleIsPlaying() },
            )

            Box(modifier = Modifier.width(8.dp))

            GlassButton(
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
            )
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
