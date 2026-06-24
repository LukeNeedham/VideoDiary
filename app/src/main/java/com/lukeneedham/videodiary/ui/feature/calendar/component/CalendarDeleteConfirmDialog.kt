package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.feature.common.DeleteConfirmDialog

@Composable
fun CalendarDeleteConfirmDialog(
    onConfirm: () -> Unit,
    dismiss: () -> Unit,
) {
    DeleteConfirmDialog(
        title = "Delete video?",
        onConfirm = onConfirm,
        dismiss = dismiss,
    )
}

@Preview
@Composable
internal fun PreviewCalendarDeleteConfirmDialog() {
    CalendarDeleteConfirmDialog(
        onConfirm = {},
        dismiss = {},
    )
}
