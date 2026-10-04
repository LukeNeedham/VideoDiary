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

/** Toolbar content for subpages: just a back button (if [canGoBack]). */
@Composable
fun SubpageToolbar(
    canGoBack: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (canGoBack) {
            FlatIconButton(
                iconRes = R.drawable.back,
                contentDescription = "Back",
                onClick = onBack,
                selected = true,
                size = 44.dp,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSubpageToolbar() {
    SubpageToolbar(
        canGoBack = true,
        onBack = {},
    )
}
