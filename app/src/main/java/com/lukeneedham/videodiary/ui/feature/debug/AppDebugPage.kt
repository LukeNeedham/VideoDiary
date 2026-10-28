package com.lukeneedham.videodiary.ui.feature.debug

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel

/**
 * VideoDiary's own debug page. The shared library's debug page (animation speed, logs, crashes) has no
 * slot for app-specific tools, so they live here, with a row that opens the library's page.
 */
@Composable
fun AppDebugPage(
    canGoBack: Boolean,
    onBack: () -> Unit,
    onOpenSdkDebug: () -> Unit,
) {
    val viewModel = koinViewModel<AppDebugViewModel>()
    val allowRetakeForPastDays by viewModel.allowRetakeForPastDays.collectAsState()

    val fakeSdCardEnabled by viewModel.fakeSdCardEnabled.collectAsState()
    val fakeSdCardInserted by viewModel.fakeSdCardInserted.collectAsState()
    val slowVideoMoves by viewModel.slowVideoMoves.collectAsState()

    AppDebugPageContent(
        onFillWithMockDataClick = viewModel::fillWithMockData,
        allowRetakeForPastDays = allowRetakeForPastDays,
        onAllowRetakeForPastDaysChange = viewModel::setAllowRetakeForPastDays,
        onResyncThumbnailsClick = viewModel::resyncThumbnails,
        fakeSdCardEnabled = fakeSdCardEnabled,
        onFakeSdCardEnabledChange = viewModel::setFakeSdCardEnabled,
        fakeSdCardInserted = fakeSdCardInserted,
        onFakeSdCardInsertedChange = viewModel::setFakeSdCardInserted,
        slowVideoMoves = slowVideoMoves,
        onSlowVideoMovesChange = viewModel::setSlowVideoMoves,
        onOpenSdkDebug = onOpenSdkDebug,
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
