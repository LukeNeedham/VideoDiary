package com.lukeneedham.videodiary.ui.feature.calendar.component

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CalendarDeleteConfirmDialog(
    onConfirm: () -> Unit,
    dismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = {
            dismiss()
        },
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

        Column(
            modifier = Modifier
                .background(color = Color.White)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Delete today's video?")
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    text = "Cancel",
                    onClick = {
                        dismiss()
                    }
                )
                Spacer(modifier = Modifier.width(15.dp))
                Button(
                    text = "Delete",
                    onClick = {
                        onConfirm()
                        dismiss()
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarDeleteConfirmDialog() {
    CalendarDeleteConfirmDialog(
        onConfirm = {},
        dismiss = {},
    )
}