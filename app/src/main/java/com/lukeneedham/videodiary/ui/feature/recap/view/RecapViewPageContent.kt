package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoQueuePlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoToolbarLayout
import com.lukeneedham.videodiary.ui.feature.recap.share.MockDataRecapShare
import com.lukeneedham.videodiary.ui.feature.recap.share.RecapShareSheet
import com.lukeneedham.videodiary.ui.feature.recap.share.model.RecapShareState
import com.lukeneedham.videodiary.ui.feature.recap.view.component.RecapViewBottomBar
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
    shareState: RecapShareState,
    includeDateStamp: Boolean,
    onIncludeDateStampChange: (Boolean) -> Unit,
    onCreateExportClick: () -> Unit,
    onCancelExportClick: () -> Unit,
    onRetryAfterFailureClick: () -> Unit,
    onManualShareClick: () -> Unit,
) {
    val controller = remember {
        VideoPlayerController().apply {
            isVolumeOn = true
        }
    }

    var isShareSheetVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        VideoToolbarLayout(
            videoAspectRatio = videoAspectRatio,
            bottomBar = {
                RecapViewBottomBar(
                    name = name,
                    isSaved = isSaved,
                    canGoBack = canGoBack,
                    onBack = onBack,
                    onToggleSavedClick = onToggleSavedClick,
                    onShareClick = { isShareSheetVisible = true },
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

        RecapShareSheet(
            visible = isShareSheetVisible,
            state = shareState,
            includeDateStamp = includeDateStamp,
            onIncludeDateStampChange = onIncludeDateStampChange,
            onCreateClick = onCreateExportClick,
            onCancelClick = onCancelExportClick,
            onRetryClick = onRetryAfterFailureClick,
            onShareClick = onManualShareClick,
            onDismiss = { isShareSheetVisible = false },
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
            shareState = RecapShareState.SelectingOptions,
            includeDateStamp = false,
            onIncludeDateStampChange = {},
            onCreateExportClick = {},
            onCancelExportClick = {},
            onRetryAfterFailureClick = {},
            onManualShareClick = {},
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
            shareState = MockDataRecapShare.ready,
            includeDateStamp = false,
            onIncludeDateStampChange = {},
            onCreateExportClick = {},
            onCancelExportClick = {},
            onRetryAfterFailureClick = {},
            onManualShareClick = {},
        )
    }
}
