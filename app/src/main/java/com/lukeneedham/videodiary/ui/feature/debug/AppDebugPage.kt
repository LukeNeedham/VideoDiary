package com.lukeneedham.videodiary.ui.feature.debug

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

/**
 * VideoDiary's own debug page. The shared library's debug page (animation speed, logs, crashes) has no
 * slot for app-specific tools, so they live here, with a row that opens the library's page.
 */
@Composable
fun AppDebugPage(
    viewModel: AppDebugViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onOpenSdkDebug: () -> Unit,
) {
    val allowRetakeForPastDays by viewModel.allowRetakeForPastDays.collectAsState()

    AppDebugPageContent(
        onFillWithMockDataClick = viewModel::fillWithMockData,
        allowRetakeForPastDays = allowRetakeForPastDays,
        onAllowRetakeForPastDaysChange = viewModel::setAllowRetakeForPastDays,
        onResyncThumbnailsClick = viewModel::resyncThumbnails,
        onOpenSdkDebug = onOpenSdkDebug,
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
