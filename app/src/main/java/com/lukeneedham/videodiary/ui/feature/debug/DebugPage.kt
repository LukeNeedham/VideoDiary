package com.lukeneedham.videodiary.ui.feature.debug

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun DebugPage(
    viewModel: DebugViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    val allowRetakeForPastDays by viewModel.allowRetakeForPastDays.collectAsState()

    DebugPageContent(
        onFillWithMockDataClick = viewModel::fillWithMockData,
        allowRetakeForPastDays = allowRetakeForPastDays,
        onAllowRetakeForPastDaysChange = viewModel::setAllowRetakeForPastDays,
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
