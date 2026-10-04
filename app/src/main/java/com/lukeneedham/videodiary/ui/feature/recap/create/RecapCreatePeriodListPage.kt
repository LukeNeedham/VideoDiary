package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.ui.feature.common.toolbar.VideoAspectRatioViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodOption

@Composable
fun RecapCreatePeriodListPage(
    viewModel: RecapCreatePeriodListViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onOptionClick: (RecapPeriodOption) -> Unit,
) {
    val aspectRatioViewModel = koinViewModel<VideoAspectRatioViewModel>()

    RecapCreatePeriodListPageContent(
        videoAspectRatio = aspectRatioViewModel.videoAspectRatio,
        periodType = viewModel.periodType,
        options = viewModel.options,
        isLoaded = viewModel.isLoaded,
        canGoBack = canGoBack,
        onBack = onBack,
        onOptionClick = onOptionClick,
    )
}
