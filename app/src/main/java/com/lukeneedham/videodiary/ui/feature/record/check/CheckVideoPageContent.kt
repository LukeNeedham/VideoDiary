package com.lukeneedham.videodiary.ui.feature.record.check

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.record.check.component.CheckVideoTile
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun CheckVideoPageContent(
    existingVideo: Video,
    newVideo: Video,
    videoAspectRatio: Float,
    onCloseClick: () -> Unit,
    onRetakeClick: () -> Unit,
    onExistingVideoSelected: () -> Unit,
    onNewVideoSelected: () -> Unit,
) {
    val existingController = remember {
        VideoPlayerController().apply { playingVideo = existingVideo }
    }
    val newController = remember {
        VideoPlayerController().apply { playingVideo = newVideo }
    }

    // Pressing and holding either video pauses both, so the user can compare a paused frame
    // without one video racing ahead of the other.
    val pauseBoth: () -> Unit = remember(existingController, newController) {
        {
            existingController.temporaryPause()
            newController.temporaryPause()
        }
    }
    val resumeBoth: () -> Unit = remember(existingController, newController) {
        {
            existingController.temporaryResume()
            newController.temporaryResume()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Tap the video you want to keep",
                color = Color.White,
                fontSize = Typography.Size.medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 16.dp, bottom = 8.dp, start = 64.dp, end = 64.dp),
            )

            GlassIconButton(
                iconRes = R.drawable.close,
                contentDescription = "Close",
                onClick = onCloseClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(16.dp),
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                CheckVideoTile(
                    label = "EXISTING",
                    video = existingVideo,
                    controller = existingController,
                    aspectRatio = videoAspectRatio,
                    onSelected = onExistingVideoSelected,
                    onPress = pauseBoth,
                    onRelease = resumeBoth,
                    modifier = Modifier.weight(1f),
                )
                CheckVideoTile(
                    label = "NEW",
                    video = newVideo,
                    controller = newController,
                    aspectRatio = videoAspectRatio,
                    onSelected = onNewVideoSelected,
                    onPress = pauseBoth,
                    onRelease = resumeBoth,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp),
        ) {
            GlassIconButton(
                iconRes = if (existingController.isVolumeOn) R.drawable.volume_on else R.drawable.volume_off,
                contentDescription = "Toggle existing video sound",
                onClick = { existingController.toggleVolumeOn() },
                modifier = Modifier.align(Alignment.CenterStart),
            )

            GlassIconButton(
                iconRes = R.drawable.retake,
                contentDescription = "Retake",
                onClick = onRetakeClick,
                modifier = Modifier.align(Alignment.Center),
            )

            GlassIconButton(
                iconRes = if (newController.isVolumeOn) R.drawable.volume_on else R.drawable.volume_off,
                contentDescription = "Toggle new video sound",
                onClick = { newController.toggleVolumeOn() },
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewCheckVideoPageContent() {
    CheckVideoPageContent(
        existingVideo = MockDataCheckVideo.existingVideo,
        newVideo = MockDataCheckVideo.newVideo,
        videoAspectRatio = 0.5625f,
        onCloseClick = {},
        onRetakeClick = {},
        onExistingVideoSelected = {},
        onNewVideoSelected = {},
    )
}
