package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.runtime.Composable

@Composable
fun RecapCreateTypePage(
    viewModel: RecapCreateTypeViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onCreateMonthClick: () -> Unit,
    onCreateWeekClick: () -> Unit,
    onCreateYearClick: () -> Unit,
    onCreateCustomClick: () -> Unit,
) {
    RecapCreateTypePageContent(
        videoAspectRatio = viewModel.videoAspectRatio,
        canGoBack = canGoBack,
        onBack = onBack,
        onCreateMonthClick = onCreateMonthClick,
        onCreateWeekClick = onCreateWeekClick,
        onCreateYearClick = onCreateYearClick,
        onCreateCustomClick = onCreateCustomClick,
    )
}
