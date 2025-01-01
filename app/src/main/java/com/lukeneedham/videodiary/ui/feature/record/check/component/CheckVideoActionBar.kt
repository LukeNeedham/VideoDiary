package com.lukeneedham.videodiary.ui.feature.record.check.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.Button

@Composable
fun CheckVideoActionBar(
    onCancelClick: () -> Unit,
    onRetakeClick: () -> Unit,
    onAccepted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 5.dp, top = 5.dp)
    ) {
        @Composable
        fun ActionButton(
            text: String,
            onClick: () -> Unit,
        ) {
            Button(
                text = text,
                onClick = onClick,
                backgroundColor = Color.White,
                foregroundColor = Color.Black,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }

        ActionButton(
            text = "Cancel",
            onClick = onCancelClick,
        )

        ActionButton(
            text = "Retake",
            onClick = onRetakeClick,
        )

        ActionButton(
            text = "Accept",
            onClick = onAccepted,
        )
    }
}

@Preview
@Composable
internal fun PreviewCheckVideoActionBar() {
    CheckVideoActionBar(
        onRetakeClick = {},
        onAccepted = {},
        onCancelClick = {},
    )
}