package com.lukeneedham.videodiary.ui.feature.setup.resolution

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ResolutionSelector(
    currentResolutionName: String?,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (currentResolutionName != null) {
            Text(
                text = currentResolutionName,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewResolutionSelector() {
    ResolutionSelector(
        currentResolutionName = "100x200",
    )
}
