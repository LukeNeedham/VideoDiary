package com.lukeneedham.videodiary.ui.feature.exportdiary.progress

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.domain.model.ExportedVideo

@Composable
fun ExportDiaryProgressPage(
    viewModel: ExportDiaryProgressViewModel,
    onExported: (exportedVideo: ExportedVideo) -> Unit,
    onExit: () -> Unit,
) {
    LaunchedEffect(viewModel) {
        viewModel.onExportedFlow.collect {
            onExported(it)
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.onExitFlow.collect {
            onExit()
        }
    }

    ExportDiaryProgressPageContent(
        progressState = viewModel.progressState,
        isCancelling = viewModel.isCancelling,
        onCancelClick = viewModel::cancel,
        onBackClick = viewModel::dismissFailure,
    )
}
