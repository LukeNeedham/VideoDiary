package com.lukeneedham.videodiary.ui.feature.recap.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.domain.model.ShareRequest
import java.time.LocalDate
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RecapViewPage(
    startDate: LocalDate,
    endDate: LocalDate,
    name: String,
    savedRecapId: String?,
    canGoBack: Boolean,
    onBack: () -> Unit,
    share: (ShareRequest) -> Unit,
) {
    val viewModel = koinViewModel<RecapViewViewModel> {
        parametersOf(startDate, endDate, name, savedRecapId)
    }

    LaunchedEffect(viewModel) {
        viewModel.onShareFlow.collect { share(it) }
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
        shareState = viewModel.shareState,
        includeDateStamp = viewModel.includeDateStamp,
        onIncludeDateStampChange = viewModel::onIncludeDateStampChange,
        onCreateExportClick = viewModel::startExport,
        onCancelExportClick = viewModel::cancelExport,
        onBackToOptionsClick = viewModel::backToOptions,
        onManualShareClick = viewModel::shareClicked,
    )
}
