package com.lukeneedham.videodiary.ui.feature.exportdiary.hub

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.SavedExport

@Composable
fun ExportHubPage(
    viewModel: ExportHubViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onCreateExportClick: () -> Unit,
    onExportClick: (SavedExport) -> Unit,
) {
    ExportHubPageContent(
        savedExports = viewModel.savedExports,
        canGoBack = canGoBack,
        onBack = onBack,
        onCreateExportClick = onCreateExportClick,
        onExportClick = onExportClick,
        onDeleteClick = viewModel::deleteExport,
    )
}
