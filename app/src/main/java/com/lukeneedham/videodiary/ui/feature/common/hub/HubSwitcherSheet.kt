package com.lukeneedham.videodiary.ui.feature.common.hub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.theme.AppSurface
import com.lukeneedham.videodiary.ui.theme.Typography

private const val GRID_COLUMNS = 3

private val InactiveBackground = Color(0xFF3A3A3A)
private val ActiveBackground = Color.White

/** The content of the hub switcher bottom sheet: a grid of all hubs, highlighting the [currentHub]. */
@Composable
fun HubSwitcherSheet(
    currentHub: Hub?,
    onHubClick: (Hub) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(AppSurface)
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        Hub.entries.chunked(GRID_COLUMNS).forEach { rowHubs ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
            ) {
                rowHubs.forEach { hub ->
                    HubSwitcherItem(
                        hub = hub,
                        isCurrent = hub == currentHub,
                        onClick = { onHubClick(hub) },
                    )
                }
                // Keep cells in an incomplete last row the same width as the rest
                repeat(GRID_COLUMNS - rowHubs.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun RowScope.HubSwitcherItem(
    hub: Hub,
    isCurrent: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = if (isCurrent) Color.Black else Color.White
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isCurrent) ActiveBackground else InactiveBackground)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 14.dp)
    ) {
        Image(
            painter = painterResource(hub.iconRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(contentColor),
            modifier = Modifier.size(28.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = hub.title,
            color = contentColor,
            fontSize = Typography.Size.small,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = hub.description,
            color = contentColor.copy(alpha = 0.7f),
            fontSize = Typography.Size.extraSmall,
            textAlign = TextAlign.Center,
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
