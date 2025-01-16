package com.lukeneedham.videodiary.ui.feature.exportdiary.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.domain.model.ExportedVideo

@Composable
fun ExportDiaryCreatePage(
    viewModel: ExportDiaryCreateViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onExported: (exportedVideo: ExportedVideo) -> Unit,
) {
    LaunchedEffect(viewModel) {
        viewModel.onExportedFlow.collect {
            onExported(it)
        }
    }

    ExportDiaryCreatePageContent(
        canGoBack = canGoBack,
        onBack = onBack,
        totalVideoCount = viewModel.totalVideoCount,
        exportState = viewModel.exportState,
        export = viewModel::export,
        exportStartDate = viewModel.exportStartDate,
        exportEndDate = viewModel.exportEndDate,
        selectedVideoCount = viewModel.selectedVideoCount,
        diaryStartDate = viewModel.diaryStartDate,
        onStartDateSelected = {
            if (it != null) {
                viewModel.exportStartDate = it
            }
        },
        onEndDateSelected = {
            viewModel.exportEndDate = it
        },
    )
}