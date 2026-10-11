package com.lukeneedham.videodiary.ui.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.BuildConfig
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.toolbar.HubToolbar
import com.lukeneedham.videodiary.ui.feature.common.toolbar.ToolbarPageLayout
import com.lukeneedham.videodiary.ui.feature.settings.component.SettingsCard
import com.lukeneedham.videodiary.ui.theme.AppBackground

@Composable
fun SettingsHubPageContent(
    onMenuClick: () -> Unit,
    onVideoStorageClick: () -> Unit,
    onDebugClick: () -> Unit,
) {
    ToolbarPageLayout(
        bottomBar = { HubToolbar(onMenuClick = onMenuClick) },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            SettingsCard(
                title = "Video storage",
                iconRes = R.drawable.movie,
                onClick = onVideoStorageClick,
            )
            if (BuildConfig.DEBUG) {
                SettingsCard(
                    title = "Debug settings",
                    iconRes = R.drawable.bug,
                    onClick = onDebugClick,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewSettingsHubPageContent() {
    SettingsHubPageContent(
        onMenuClick = {},
        onVideoStorageClick = {},
        onDebugClick = {},
    )
}
