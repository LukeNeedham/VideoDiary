package com.lukeneedham.videodiary.ui.feature.exportdiary.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton
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
            playingVideo = Video.PersistedFile(videoFile)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            ExportDiaryViewToolbar(
                exportedVideo = exportedVideo,
                canGoBack = canGoBack,
                onBack = onBack,
            )
        }

        if (videoAspectRatio != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(videoAspectRatio)
            ) {
                VideoPlayer(
                    video = Video.PersistedFile(videoFile),
                    aspectRatio = videoAspectRatio,
                    controller = controller,
                    modifier = Modifier.fillMaxSize(),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
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
                }

                GlassIconButton(
                    iconRes = R.drawable.share,
                    contentDescription = "Share",
                    onClick = {
                        val shareText = "Full Video Diary"
                        val request = ShareRequest(
                            title = shareText,
                            text = shareText,
                            video = videoFile,
                        )
                        share(request)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                )
            }
        }
    }
}

@Composable
private fun ExportDiaryViewToolbar(
    exportedVideo: ExportedVideo,
    canGoBack: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Box(modifier = Modifier.size(50.dp)) {
                if (canGoBack) {
                    ToolbarIconButton(
                        iconRes = R.drawable.back,
                        contentDescription = "Back",
                        onClick = onBack,
                    )
                }
            }

            val name = exportedVideo.name
            val start = exportedVideo.startDate.format(StandardDateTimeFormatter.date)
            val end = exportedVideo.endDate.format(StandardDateTimeFormatter.date)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                if (name != null) {
                    Text(
                        text = name,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = Typography.Size.medium,
                    )
                    Text(
                        text = "$start to $end",
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        fontSize = Typography.Size.extraSmall,
                    )
                } else {
                    Text(
                        text = "$start to $end",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = Typography.Size.medium,
                    )
                }
            }

            Box(modifier = Modifier.size(50.dp))
        }
    }
}

@Composable
private fun ToolbarIconButton(
    iconRes: Int,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(24.dp),
        )
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
private fun PreviewPortraitUnnamed() {
    Box(
        modifier = Modifier
            .height(3000.dp)
            .width(1000.dp)
    ) {
        ExportDiaryViewPageContent(
            exportedVideo = MockDataExportDiaryView.exportedVideoUnnamed,
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
