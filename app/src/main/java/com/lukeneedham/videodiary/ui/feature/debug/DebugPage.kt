package com.lukeneedham.videodiary.ui.feature.debug

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.lukeneedham.videodiary.ui.feature.common.toolbar.VideoAspectRatioViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DebugPage(
    viewModel: DebugViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onCrashLogClick: () -> Unit,
) {
    val allowRetakeForPastDays by viewModel.allowRetakeForPastDays.collectAsState()

    val aspectRatioViewModel = koinViewModel<VideoAspectRatioViewModel>()

    DebugPageContent(
        videoAspectRatio = aspectRatioViewModel.videoAspectRatio,
        onFillWithMockDataClick = viewModel::fillWithMockData,
        allowRetakeForPastDays = allowRetakeForPastDays,
        onAllowRetakeForPastDaysChange = viewModel::setAllowRetakeForPastDays,
        onResyncThumbnailsClick = viewModel::resyncThumbnails,
        onCrashLogClick = onCrashLogClick,
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
