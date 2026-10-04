package com.lukeneedham.videodiary.ui.feature.settings

import androidx.compose.runtime.Composable

@Composable
fun SettingsHubPage(
    onMenuClick: () -> Unit,
    onDebugClick: () -> Unit,
) {
    SettingsHubPageContent(
        onMenuClick = onMenuClick,
        onDebugClick = onDebugClick,
    )
}
