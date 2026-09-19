package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapExportRequest

@Composable
fun RecapViewPage(
    viewModel: RecapViewViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onExportRequested: (exportRequest: RecapExportRequest) -> Unit,
) {
    RecapViewPageContent(
        name = viewModel.name,
        startDate = viewModel.startDate,
        endDate = viewModel.endDate,
        videoAspectRatio = viewModel.videoAspectRatio,
        videoFiles = viewModel.videoFiles,
        dayThumbnails = viewModel.dayThumbnails,
        isSaved = viewModel.isSaved,
        onToggleSavedClick = viewModel::toggleSaved,
        includeDateStamp = viewModel.includeDateStamp,
        setIncludeDateStamp = { viewModel.includeDateStamp = it },
        onExportClick = {
            viewModel.exportRequest?.let(onExportRequested)
        },
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
