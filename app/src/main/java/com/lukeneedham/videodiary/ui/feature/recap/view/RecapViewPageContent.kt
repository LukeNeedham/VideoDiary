package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassButton
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton
import com.lukeneedham.videodiary.ui.feature.common.glass.VideoControlsRow
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoQueuePlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoToolbarLayout
import com.lukeneedham.videodiary.ui.theme.Typography
import java.io.File

@Composable
fun RecapViewPageContent(
    name: String,
    videoAspectRatio: Float?,
    videoFiles: List<File>,
    isSaved: Boolean,
    onToggleSavedClick: () -> Unit,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    val controller = remember {
        VideoPlayerController().apply {
            isVolumeOn = true
        }
    }

    VideoToolbarLayout(
        videoAspectRatio = videoAspectRatio,
        bottomBar = {
            VideoControlsRow {
                Box(modifier = Modifier.size(50.dp)) {
                    if (canGoBack) {
                        GlassIconButton(
                            iconRes = R.drawable.close,
                            contentDescription = "Close",
                            onClick = onBack,
                        )
                    }
                }

                Text(
                    text = name,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = Typography.Size.medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )

                GlassButton(
                    text = if (isSaved) "Saved" else "Save",
                    onClick = onToggleSavedClick,
                )
            }
        },
    ) { aspectRatio ->
        VideoQueuePlayer(
            videoFiles = videoFiles,
            aspectRatio = aspectRatio,
            controller = controller,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun PreviewPortrait() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        RecapViewPageContent(
            name = MockDataRecapView.name,
            videoAspectRatio = 1f,
            videoFiles = MockDataRecapView.videoFiles,
            isSaved = false,
            onToggleSavedClick = {},
            canGoBack = true,
            onBack = {},
        )
    }
}

@Preview
@Composable
private fun PreviewSaved() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        RecapViewPageContent(
            name = MockDataRecapView.name,
            videoAspectRatio = 1f,
            videoFiles = MockDataRecapView.videoFiles,
            isSaved = true,
            onToggleSavedClick = {},
            canGoBack = true,
            onBack = {},
        )
    }
}
