package com.lukeneedham.videodiary.ui.feature.recap.hub

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.SavedRecap

@Composable
fun RecapHubPage(
    viewModel: RecapHubViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onCreateMonthClick: () -> Unit,
    onCreateWeekClick: () -> Unit,
    onCreateYearClick: () -> Unit,
    onCreateCustomClick: () -> Unit,
    onRecapClick: (SavedRecap) -> Unit,
) {
    RecapHubPageContent(
        savedRecaps = viewModel.savedRecaps,
        videoAspectRatio = viewModel.videoAspectRatio,
        canGoBack = canGoBack,
        onBack = onBack,
        onCreateMonthClick = onCreateMonthClick,
        onCreateWeekClick = onCreateWeekClick,
        onCreateYearClick = onCreateYearClick,
        onCreateCustomClick = onCreateCustomClick,
        onRecapClick = onRecapClick,
        onDeleteClick = viewModel::deleteRecap,
    )
}
