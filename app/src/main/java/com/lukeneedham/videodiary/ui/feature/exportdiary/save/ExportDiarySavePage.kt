package com.lukeneedham.videodiary.ui.feature.exportdiary.save

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.ExportedVideo

@Composable
fun ExportDiarySavePage(
    viewModel: ExportDiarySaveViewModel,
    exportedVideo: ExportedVideo,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onSaved: () -> Unit,
) {
    ExportDiarySavePageContent(
        exportedVideo = exportedVideo,
        name = viewModel.name,
        onNameChange = viewModel::setExportName,
        onSaveClick = { viewModel.save(onSaved) },
        isSaving = viewModel.isSaving,
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
