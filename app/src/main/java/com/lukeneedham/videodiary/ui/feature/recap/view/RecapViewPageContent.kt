package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterialApi::class)
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
    onBackToOptionsClick: () -> Unit,
    onManualShareClick: () -> Unit,
) {
    val controller = remember {
        VideoPlayerController().apply {
            isVolumeOn = true
        }
    }

    // A tween (rather than the default spring) settles - and flips sheetState.currentValue -
    // right when the sheet visually stops moving. The default spring's long, barely-visible
    // settle tail left currentValue (and so RecapShareSheet's hidden-content gating) lagging
    // behind by about a second, during which the sheet's content was still fully mounted and
    // sitting in the - unclipped, since Root pads the whole app away from the system bars -
    // navigation bar's area.
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = tween(durationMillis = 250),
    )
    val coroutineScope = rememberCoroutineScope()

    RecapShareSheet(
        sheetState = sheetState,
        state = shareState,
        includeDateStamp = includeDateStamp,
        onIncludeDateStampChange = onIncludeDateStampChange,
        onCreateClick = onCreateExportClick,
        onCancelClick = onCancelExportClick,
        onBackToOptionsClick = onBackToOptionsClick,
        onShareClick = onManualShareClick,
    ) {
        VideoToolbarLayout(
            videoAspectRatio = videoAspectRatio,
            bottomBar = {
                RecapViewBottomBar(
                    name = name,
                    isSaved = isSaved,
                    canGoBack = canGoBack,
                    onBack = onBack,
                    onToggleSavedClick = onToggleSavedClick,
                    onShareClick = { coroutineScope.launch { sheetState.show() } },
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
            onBackToOptionsClick = {},
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
            onBackToOptionsClick = {},
            onManualShareClick = {},
        )
    }
}
