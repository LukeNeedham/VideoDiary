package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.runtime.Composable

@Composable
fun RecapViewPage(
    viewModel: RecapViewViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    RecapViewPageContent(
        name = viewModel.name,
        videoAspectRatio = viewModel.videoAspectRatio,
        videoFiles = viewModel.videoFiles,
        isSaved = viewModel.isSaved,
        onToggleSavedClick = viewModel::toggleSaved,
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
