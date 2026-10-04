package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.recap.share.RecapShareViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RecapViewPage(
    viewModel: RecapViewViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    share: (ShareRequest) -> Unit,
) {
    val shareViewModel = koinViewModel<RecapShareViewModel> {
        parametersOf(viewModel.startDate, viewModel.endDate, viewModel.name)
    }

    LaunchedEffect(shareViewModel, viewModel.name) {
        shareViewModel.name = viewModel.name
    }

    LaunchedEffect(shareViewModel, viewModel.days) {
        shareViewModel.updateDays(viewModel.days)
    }

    LaunchedEffect(shareViewModel) {
        shareViewModel.onShareFlow.collect { share(it) }
    }

    RecapViewPageContent(
        name = viewModel.name,
        onNameChange = viewModel::rename,
        startDate = viewModel.startDate,
        endDate = viewModel.endDate,
        thumbnails = viewModel.thumbnails,
        videoFiles = viewModel.videoFiles,
        isSaved = viewModel.isSaved,
        onToggleSavedClick = viewModel::toggleSaved,
        canGoBack = canGoBack,
        onBack = onBack,
        shareState = shareViewModel.state,
        includeDateStamp = shareViewModel.includeDateStamp,
        onIncludeDateStampChange = shareViewModel::onIncludeDateStampChange,
        onCreateExportClick = shareViewModel::startExport,
        onCancelExportClick = shareViewModel::cancelExport,
        onBackToOptionsClick = shareViewModel::backToOptions,
        onManualShareClick = shareViewModel::shareClicked,
    )
}
