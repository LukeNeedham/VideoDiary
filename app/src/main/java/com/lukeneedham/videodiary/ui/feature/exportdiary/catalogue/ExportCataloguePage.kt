package com.lukeneedham.videodiary.ui.feature.exportdiary.catalogue

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.SavedExport

@Composable
fun ExportCataloguePage(
    viewModel: ExportCatalogueViewModel,
    onExportClick: (SavedExport) -> Unit,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    ExportCataloguePageContent(
        savedExports = viewModel.savedExports,
        onExportClick = onExportClick,
        onDeleteClick = viewModel::deleteExport,
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
