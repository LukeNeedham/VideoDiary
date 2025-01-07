package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DiaryDatePickerDialogScaffold(
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    content: @Composable () -> Unit,
) {
    @Composable
    fun Button(
        text: String,
        onClick: () -> Unit,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .heightIn(min = 50.dp)
                .widthIn(min = 50.dp)
                .clickable {
                    onClick()
                }
        ) {
            Text(
                text = text,
                color = Color.Black,
            )
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .background(color = Color.White)
        ) {
            content()
            Spacer(modifier = Modifier.height(5.dp))

            Row {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    text = "Cancel",
                    onClick = onDismiss,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    text = "Ok",
                    onClick = onAccept,
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Preview
@Composable
internal fun PreviewDatePickerDialog() {
    DiaryDatePickerDialogScaffold(
        onDismiss = {},
        onAccept = {},
    ) {
        Text(text = "Sample!")
    }
}