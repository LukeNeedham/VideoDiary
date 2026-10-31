package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetDefaults
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetLayout
import com.lukeneedham.videodiary.ui.theme.AppTheme
import kotlinx.coroutines.launch

/** A bottom sheet for choosing which type of recap to create, shown as a grid of options. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecapCreateTypeSheet(
    onWeekClick: () -> Unit,
    onMonthClick: () -> Unit,
    onYearClick: () -> Unit,
    onCustomClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SheetDefaults.containerColor,
        contentColor = SheetDefaults.contentColor,
        shape = SheetDefaults.shape,
        scrimColor = SheetDefaults.scrimColor,
        dragHandle = null,
        // Handled by SheetLayout, so that the sheet can animate fully out
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
    ) {
        SheetLayout(
            onClose = {
                scope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
            },
        ) {
            RecapCreateTypeSheetContent(
                onWeekClick = onWeekClick,
                onMonthClick = onMonthClick,
                onYearClick = onYearClick,
                onCustomClick = onCustomClick,
                // Dismiss first, so the sheet is not still open when coming back
                onChosen = onDismiss,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRecapCreateTypeSheet() {
    AppTheme {
        RecapCreateTypeSheet(
            onWeekClick = {},
            onMonthClick = {},
            onYearClick = {},
            onCustomClick = {},
            onDismiss = {},
        )
    }
}
