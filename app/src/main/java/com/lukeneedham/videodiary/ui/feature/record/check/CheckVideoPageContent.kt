package com.lukeneedham.videodiary.ui.feature.record.check

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassButton
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoToolbarLayout
import com.lukeneedham.videodiary.ui.feature.record.check.component.CheckVideoTile
import com.lukeneedham.videodiary.ui.feature.record.check.component.ChooseVideoButton
import com.lukeneedham.videodiary.ui.feature.record.film.component.RecordBarIconButton
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun CheckVideoPageContent(
    existingVideo: Video,
    newVideo: Video,
    videoAspectRatio: Float,
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

    // Only one video can be audible at a time: unmuting one forcibly mutes the other, but both
    // can be muted together.
    val toggleExistingVolume: () -> Unit = remember(existingController, newController) {
        {
            if (existingController.isVolumeOn) {
                existingController.isVolumeOn = false
            } else {
                existingController.isVolumeOn = true
                newController.isVolumeOn = false
            }
        }
    }
    val toggleNewVolume: () -> Unit = remember(existingController, newController) {
        {
            if (newController.isVolumeOn) {
                newController.isVolumeOn = false
            } else {
                newController.isVolumeOn = true
                existingController.isVolumeOn = false
            }
        }
    }

    VideoToolbarLayout(
        videoAspectRatio = videoAspectRatio,
        topOverlay = {
            Text(
                text = "Choose the video to keep",
                color = Color.White,
                fontSize = Typography.Size.medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 8.dp),
            )
        },
        bottomBar = {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f),
                    ) {
                        val muteIcon =
                            if (existingController.isVolumeOn) R.drawable.volume_on else R.drawable.volume_off
                        RecordBarIconButton(
                            iconRes = muteIcon,
                            contentDescription = "Toggle existing video sound",
                            onClick = toggleExistingVolume,
                        )
                        Spacer(Modifier.height(4.dp))
                        ChooseVideoButton(
                            label = "EXISTING",
                            onClick = onExistingVideoSelected,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f),
                    ) {
                        val muteIcon =
                            if (newController.isVolumeOn) R.drawable.volume_on else R.drawable.volume_off
                        RecordBarIconButton(
                            iconRes = muteIcon,
                            contentDescription = "Toggle new video sound",
                            onClick = toggleNewVolume,
                        )
                        Spacer(Modifier.height(4.dp))
                        ChooseVideoButton(
                            label = "NEW",
                            onClick = onNewVideoSelected,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                GlassButton(
                    text = "Retake",
                    onClick = onRetakeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                )
            }
        },
    ) { aspectRatio ->
        // Each tile keeps the video's true aspect ratio, so side by side they're only half as
        // tall as a single full-width video would be - centered here in the same slot height
        // every other video page reserves, rather than shrinking that slot to fit them.
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize(),
        ) {
            CheckVideoTile(
                video = existingVideo,
                controller = existingController,
                aspectRatio = aspectRatio,
                onPress = pauseBoth,
                onRelease = resumeBoth,
                modifier = Modifier.weight(1f),
            )
            CheckVideoTile(
                video = newVideo,
                controller = newController,
                aspectRatio = aspectRatio,
                onPress = pauseBoth,
                onRelease = resumeBoth,
                modifier = Modifier.weight(1f),
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
        onRetakeClick = {},
        onExistingVideoSelected = {},
        onNewVideoSelected = {},
    )
}
