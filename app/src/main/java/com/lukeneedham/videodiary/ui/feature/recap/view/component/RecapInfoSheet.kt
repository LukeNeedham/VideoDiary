package com.lukeneedham.videodiary.ui.feature.recap.view.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDayThumbnail
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetDefaults
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetLayout
import com.lukeneedham.videodiary.ui.theme.AppTheme
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * A bottom sheet with the details of a recap: its name (which can be edited), its date range, and
 * the videos in it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecapInfoSheet(
    name: String,
    onNameChange: (String) -> Unit,
    startDate: LocalDate,
    endDate: LocalDate,
    thumbnails: List<RecapDayThumbnail>,
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
            RecapInfoSheetContent(
                name = name,
                onNameChange = onNameChange,
                startDate = startDate,
                endDate = endDate,
                thumbnails = thumbnails,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        RecapInfoSheet(
            name = "Spring Trip",
            onNameChange = {},
            startDate = LocalDate.of(2026, 3, 1),
            endDate = LocalDate.of(2026, 5, 31),
            thumbnails = emptyList(),
            onDismiss = {},
        )
    }
}
