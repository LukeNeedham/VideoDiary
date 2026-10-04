package com.lukeneedham.videodiary.ui.feature.recap.create.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.theme.AppBackground
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun RecapCreateEmpty() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            textAlign = TextAlign.Center,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = Typography.Size.medium,
            text = "There are no videos in your diary! Come back to create a recap once you have some recordings."
        )
    }
}

@Preview
@Composable
internal fun PreviewRecapCreateEmpty() {
    Box(modifier = Modifier.fillMaxSize().background(AppBackground)) {
        RecapCreateEmpty()
    }
}
