package com.lukeneedham.videodiary.ui.feature.common.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.theme.AppSurface

/** Shared styling for every bottom sheet in the app, so they all look the same. */
object SheetDefaults {
    val containerColor: Color = AppSurface
    val contentColor: Color = Color.White
    val shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    val scrimColor: Color = Color.Black.copy(alpha = 0.5f)
}

private val HandleTouchWidth = 64.dp
private val HandleTouchHeight = 32.dp
private val HandleWidth = 36.dp
private val HandleHeight = 4.dp

/**
 * The standard layout for the content of every bottom sheet: a drag handle centred at the top,
 * with [content] below it. Clicking the handle calls [onClose], which should hide the sheet.
 *
 * The sheet container itself (the Material sheet or sheet layout) should be styled with
 * [SheetDefaults]. This layout handles the navigation bar inset, so the container should not.
 */
@Composable
fun SheetLayout(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SheetDefaults.containerColor)
            .navigationBarsPadding(),
    ) {
        SheetDragHandle(
            onClick = onClose,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        content()
    }
}

@Composable
private fun SheetDragHandle(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(width = HandleTouchWidth, height = HandleTouchHeight)
            .semantics { contentDescription = "Close" }
            .clickable(role = Role.Button, onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .size(width = HandleWidth, height = HandleHeight)
                .clip(RoundedCornerShape(HandleHeight / 2))
                .background(Color.White.copy(alpha = 0.3f)),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SheetLayout(onClose = {}) {
        Box(modifier = Modifier.size(100.dp))
    }
}
