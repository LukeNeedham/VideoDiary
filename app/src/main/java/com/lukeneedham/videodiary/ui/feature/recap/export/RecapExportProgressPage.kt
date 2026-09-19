package com.lukeneedham.videodiary.ui.feature.recap.export

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.domain.model.ShareRequest

@Composable
fun RecapExportProgressPage(
    viewModel: RecapExportProgressViewModel,
    share: (ShareRequest) -> Unit,
    onExit: () -> Unit,
) {
    LaunchedEffect(viewModel) {
        viewModel.onShareFlow.collect {
            share(it)
            onExit()
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.onExitFlow.collect {
            onExit()
        }
    }

    RecapExportProgressPageContent(
        progressState = viewModel.progressState,
        isCancelling = viewModel.isCancelling,
        onCancelClick = viewModel::cancel,
        onBackClick = viewModel::dismissFailure,
    )
}
