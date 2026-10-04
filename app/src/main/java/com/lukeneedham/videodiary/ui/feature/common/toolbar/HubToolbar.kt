package com.lukeneedham.videodiary.ui.feature.common.toolbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R

/** Toolbar content for hub pages: a menu button, aligned left, which opens the hub switcher. */
@Composable
fun HubToolbar(
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        HubMenuButton(
            onClick = onMenuClick,
            modifier = Modifier.align(Alignment.CenterStart),
        )
    }
}

/** The menu (hamburger) button which opens the hub switcher. */
@Composable
fun HubMenuButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FlatIconButton(
        iconRes = R.drawable.menu,
        contentDescription = "Menu",
        onClick = onClick,
        selected = true,
        modifier = modifier.padding(4.dp),
    )
}

@Preview
@Composable
private fun PreviewHubToolbar() {
    HubToolbar(onMenuClick = {})
}
