package com.lukeneedham.videodiary.ui.feature.settings

import androidx.compose.runtime.Composable

@Composable
fun SettingsHubPage(
    onMenuClick: () -> Unit,
    onVideoStorageClick: () -> Unit,
    onDebugClick: () -> Unit,
    onLogsClick: () -> Unit,
    onCrashesClick: () -> Unit,
) {
    SettingsHubPageContent(
        onMenuClick = onMenuClick,
        onVideoStorageClick = onVideoStorageClick,
        onDebugClick = onDebugClick,
        onLogsClick = onLogsClick,
        onCrashesClick = onCrashesClick,
    )
}
