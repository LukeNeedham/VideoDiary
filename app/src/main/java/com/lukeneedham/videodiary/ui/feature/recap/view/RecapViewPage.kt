package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapShareRequest

@Composable
fun RecapViewPage(
    viewModel: RecapViewViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onShareRequested: (RecapShareRequest) -> Unit,
) {
    RecapViewPageContent(
        name = viewModel.name,
        videoAspectRatio = viewModel.videoAspectRatio,
        videoFiles = viewModel.videoFiles,
        isSaved = viewModel.isSaved,
        onToggleSavedClick = viewModel::toggleSaved,
        onShareClick = { onShareRequested(viewModel.buildShareRequest()) },
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
