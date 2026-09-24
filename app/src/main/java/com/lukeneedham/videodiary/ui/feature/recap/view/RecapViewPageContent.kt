package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoQueuePlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoToolbarLayout
import com.lukeneedham.videodiary.ui.feature.recap.view.component.RecapViewBottomBar
import java.io.File

@Composable
fun RecapViewPageContent(
    name: String,
    videoAspectRatio: Float?,
    videoFiles: List<File>,
    isSaved: Boolean,
    onToggleSavedClick: () -> Unit,
    onShareClick: () -> Unit,
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
            RecapViewBottomBar(
                name = name,
                isSaved = isSaved,
                canGoBack = canGoBack,
                onBack = onBack,
                onToggleSavedClick = onToggleSavedClick,
                onShareClick = onShareClick,
                modifier = Modifier.align(Alignment.Center),
            )
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
            onShareClick = {},
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
            onShareClick = {},
            canGoBack = true,
            onBack = {},
        )
    }
}
