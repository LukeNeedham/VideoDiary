package com.lukeneedham.videodiary.ui.feature.exportdiary.create.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ExportDiaryEmptyCreate(
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            textAlign = TextAlign.Center,
            color = Color.Black,
            text = "There are no videos in your diary! Come back to export a full diary video once you have some recordings."
        )
    }
}

@Preview
@Composable
internal fun PreviewExportDiaryEmpty() {
    ExportDiaryEmptyCreate()
}