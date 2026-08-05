package com.lukeneedham.videodiary.ui.feature.exportdiary.create

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportRequest

@Composable
fun ExportDiaryCreatePage(
    viewModel: ExportDiaryCreateViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onExportRequested: (exportRequest: ExportRequest) -> Unit,
) {
    ExportDiaryCreatePageContent(
        canGoBack = canGoBack,
        onBack = onBack,
        totalVideoCount = viewModel.totalVideoCount,
        exportStartDate = viewModel.exportStartDate,
        exportEndDate = viewModel.exportEndDate,
        selectedVideoCount = viewModel.selectedVideoCount,
        selectedDayThumbnails = viewModel.selectedDayThumbnails,
        diaryStartDate = viewModel.diaryStartDate,
        onStartDateSelected = { viewModel.exportStartDate = it },
        onEndDateSelected = { viewModel.exportEndDate = it },
        exportIncludeDateStamp = viewModel.exportIncludeDateStamp,
        setExportIncludeDateStamp = { viewModel.exportIncludeDateStamp = it },
        exportName = viewModel.exportName,
        onExportNameChange = { viewModel.exportName = it },
        export = {
            viewModel.exportRequest?.let(onExportRequested)
        },
    )
}
