package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.feature.common.DeleteConfirmDialog
import com.lukeneedham.videodiary.ui.theme.AppTheme

@Composable
fun CalendarDeleteConfirmDialog(
    onConfirm: () -> Unit,
    dismiss: () -> Unit,
) {
    DeleteConfirmDialog(
        title = "Delete this video for good?",
        onConfirm = onConfirm,
        dismiss = dismiss,
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        CalendarDeleteConfirmDialog(
            onConfirm = {},
            dismiss = {},
        )
    }
}
