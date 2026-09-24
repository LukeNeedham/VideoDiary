package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapExportRequest

@Composable
fun RecapViewPage(
    viewModel: RecapViewViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onExportRequested: (RecapExportRequest) -> Unit,
) {
    RecapViewPageContent(
        name = viewModel.name,
        videoAspectRatio = viewModel.videoAspectRatio,
        videoFiles = viewModel.videoFiles,
        isSaved = viewModel.isSaved,
        onToggleSavedClick = viewModel::toggleSaved,
        includeDateStamp = viewModel.includeDateStamp,
        onIncludeDateStampChange = viewModel::onIncludeDateStampChange,
        onShareClick = { onExportRequested(viewModel.buildExportRequest()) },
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
