package com.lukeneedham.videodiary.ui.feature.common.toolbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R

@Composable
fun GenericToolbar(
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(ToolbarSize.portraitToolbarHeight)
            .background(color = Color.Black)
            .padding(5.dp)
    ) {
        if (canGoBack) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(50.dp)
                    .clickable { onBack() }
            ) {
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "Back",
                    colorFilter = ColorFilter.tint(color = Color.White),
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewGenericToolbar() {
    GenericToolbar(
        canGoBack = true, onBack = {},
    )
}