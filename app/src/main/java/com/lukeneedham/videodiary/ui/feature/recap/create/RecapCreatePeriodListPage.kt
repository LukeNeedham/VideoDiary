package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodOption
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapPeriodType
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RecapCreatePeriodListPage(
    periodType: RecapPeriodType,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onOptionClick: (RecapPeriodOption) -> Unit,
) {
    val viewModel = koinViewModel<RecapCreatePeriodListViewModel> {
        parametersOf(periodType)
    }

    RecapCreatePeriodListPageContent(
        periodType = viewModel.periodType,
        options = viewModel.options,
        isLoaded = viewModel.isLoaded,
        canGoBack = canGoBack,
        onBack = onBack,
        onOptionClick = onOptionClick,
    )
}
