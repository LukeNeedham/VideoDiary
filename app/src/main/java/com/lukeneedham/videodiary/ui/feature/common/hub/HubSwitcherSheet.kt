package com.lukeneedham.videodiary.ui.feature.common.hub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.theme.AppSurface
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant

/** The content of the hub switcher bottom sheet: lists all hubs, highlighting the [currentHub]. */
@Composable
fun HubSwitcherSheet(
    currentHub: Hub?,
    onHubClick: (Hub) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppSurface)
            .navigationBarsPadding()
            .padding(vertical = 16.dp)
    ) {
        Hub.available.forEach { hub ->
            HubSwitcherItem(
                hub = hub,
                isCurrent = hub == currentHub,
                onClick = { onHubClick(hub) },
            )
        }
    }
}

@Composable
private fun HubSwitcherItem(
    hub: Hub,
    isCurrent: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isCurrent) AppSurfaceVariant else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Image(
            painter = painterResource(hub.iconRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = hub.title,
            color = Color.White,
        )
    }
}

@Preview
@Composable
private fun PreviewHubSwitcherSheet() {
    HubSwitcherSheet(
        currentHub = Hub.Calendar,
        onHubClick = {},
    )
}
