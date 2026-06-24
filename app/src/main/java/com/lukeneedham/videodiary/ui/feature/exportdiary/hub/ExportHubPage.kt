package com.lukeneedham.videodiary.ui.feature.exportdiary.hub

import androidx.compose.runtime.Composable

@Composable
fun ExportHubPage(
    canGoBack: Boolean,
    onBack: () -> Unit,
    onCreateExportClick: () -> Unit,
    onSavedExportsClick: () -> Unit,
) {
    ExportHubPageContent(
        canGoBack = canGoBack,
        onBack = onBack,
        onCreateExportClick = onCreateExportClick,
        onSavedExportsClick = onSavedExportsClick,
    )
}
