package com.lukeneedham.videodiary.ui.feature.debug

import androidx.compose.runtime.Composable

@Composable
fun DebugPage(
    viewModel: DebugViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    DebugPageContent(
        onFillWithMockDataClick = viewModel::fillWithMockData,
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
