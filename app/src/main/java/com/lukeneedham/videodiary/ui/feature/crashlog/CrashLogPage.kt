package com.lukeneedham.videodiary.ui.feature.crashlog

import androidx.compose.runtime.Composable

@Composable
fun CrashLogPage(
    viewModel: CrashLogViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    CrashLogPageContent(
        crashLogs = viewModel.crashLogs,
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
