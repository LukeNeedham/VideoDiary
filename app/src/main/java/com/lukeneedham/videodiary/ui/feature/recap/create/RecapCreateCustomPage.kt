package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.ui.feature.common.toolbar.VideoAspectRatioViewModel
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate

@Composable
fun RecapCreateCustomPage(
    viewModel: RecapCreateCustomViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onRecapCreated: (startDate: LocalDate, endDate: LocalDate, name: String, savedRecapId: String) -> Unit,
) {
    val aspectRatioViewModel = koinViewModel<VideoAspectRatioViewModel>()

    LaunchedEffect(viewModel) {
        viewModel.onSavedFlow.collect { args ->
            onRecapCreated(args.startDate, args.endDate, args.name, args.savedRecapId)
        }
    }

    RecapCreateCustomPageContent(
        videoAspectRatio = aspectRatioViewModel.videoAspectRatio,
        canGoBack = canGoBack,
        onBack = onBack,
        totalVideoCount = viewModel.totalVideoCount,
        recapStartDate = viewModel.recapStartDate,
        recapEndDate = viewModel.recapEndDate,
        selectedVideoCount = viewModel.selectedVideoCount,
        selectedDayThumbnails = viewModel.selectedDayThumbnails,
        diaryStartDate = viewModel.diaryStartDate,
        onStartDateSelected = { viewModel.recapStartDate = it },
        onEndDateSelected = { viewModel.recapEndDate = it },
        recapName = viewModel.recapName,
        onRecapNameChange = { viewModel.recapName = it },
        canSave = viewModel.canSave,
        onSaveClick = viewModel::save,
    )
}
