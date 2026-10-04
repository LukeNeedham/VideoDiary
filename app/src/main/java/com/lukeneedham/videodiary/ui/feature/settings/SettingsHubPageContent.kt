package com.lukeneedham.videodiary.ui.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.toolbar.HubToolbar
import com.lukeneedham.videodiary.ui.feature.common.toolbar.ToolbarPageLayout
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun SettingsHubPageContent(
    onMenuClick: () -> Unit,
    onDebugClick: () -> Unit,
) {
    ToolbarPageLayout(
        bottomBar = { HubToolbar(onMenuClick = onMenuClick) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp)
        ) {
            Text(
                text = "Debug settings",
                color = Color.Black,
                fontSize = Typography.Size.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDebugClick() }
                    .padding(vertical = 10.dp),
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewSettingsHubPageContent() {
    SettingsHubPageContent(
        onMenuClick = {},
        onDebugClick = {},
    )
}
