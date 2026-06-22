package com.lukeneedham.videodiary.ui.feature.record.film.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.theme.AccentRecord
import com.lukeneedham.videodiary.ui.theme.GlassFill
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun RecordingCountdownButton(
    remainingSeconds: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(76.dp)
            .clip(CircleShape)
            .background(AccentRecord, CircleShape)
            .border(width = 3.dp, color = Color.White, shape = CircleShape)
            .clickable(onClick = onClick),
    ) {
        Text(
            text = remainingSeconds.toString(),
            color = Color.White,
            fontSize = Typography.Size.big,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
private fun PreviewRecordingCountdownButton() {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .padding(20.dp),
    ) {
        RecordingCountdownButton(
            remainingSeconds = 7,
            onClick = {},
        )
    }
}
