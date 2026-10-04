package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.ui.feature.common.toolbar.VideoAspectRatioViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RecapCreateTypePage(
    canGoBack: Boolean,
    onBack: () -> Unit,
    onCreateMonthClick: () -> Unit,
    onCreateWeekClick: () -> Unit,
    onCreateYearClick: () -> Unit,
    onCreateCustomClick: () -> Unit,
) {
    val aspectRatioViewModel = koinViewModel<VideoAspectRatioViewModel>()

    RecapCreateTypePageContent(
        videoAspectRatio = aspectRatioViewModel.videoAspectRatio,
        canGoBack = canGoBack,
        onBack = onBack,
        onCreateMonthClick = onCreateMonthClick,
        onCreateWeekClick = onCreateWeekClick,
        onCreateYearClick = onCreateYearClick,
        onCreateCustomClick = onCreateCustomClick,
    )
}
