package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodOption

@Composable
fun RecapCreatePeriodListPage(
    viewModel: RecapCreatePeriodListViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onOptionClick: (RecapPeriodOption) -> Unit,
) {
    RecapCreatePeriodListPageContent(
        periodType = viewModel.periodType,
        options = viewModel.options,
        isLoaded = viewModel.isLoaded,
        canGoBack = canGoBack,
        onBack = onBack,
        onOptionClick = onOptionClick,
    )
}
