package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetDefaults
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetLayout
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.theme.AppTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryDatePickerSheet(
    /** All days of the diary, grouped by calendar month in chronological order */
    months: List<List<Day>>,
    /** Null until it is known: the sheet shows a placeholder meanwhile */
    videoAspectRatio: Float?,
    initialFocusedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    onJumpToToday: (() -> Unit)? = null,
    /** If set, days before this date are shown but can't be selected */
    minDate: LocalDate? = null,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    /** Animates the sheet away, then dismisses it. */
    fun hideThen(action: () -> Unit) {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            action()
            onDismiss()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SheetDefaults.containerColor,
        contentColor = SheetDefaults.contentColor,
        shape = SheetDefaults.shape,
        scrimColor = SheetDefaults.scrimColor,
        dragHandle = null,
        // SheetLayout handles the nav bar inset itself, rather than the sheet window, so that the
        // sheet can animate fully out without getting stuck under the nav bar.
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
    ) {
        SheetLayout(onClose = { hideThen {} }) {
            DiaryDatePickerSheetContent(
                months = months,
                videoAspectRatio = videoAspectRatio,
                initialFocusedDate = initialFocusedDate,
                onDateSelected = { hideThen { onDateSelected(it) } },
                onJumpToToday = onJumpToToday?.let { jump -> { hideThen(jump) } },
                minDate = minDate,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        DiaryDatePickerSheet(
            months = MockDataDiaryDatePicker.months,
            videoAspectRatio = MockDataDiaryDatePicker.videoAspectRatio,
            initialFocusedDate = MockDataDiaryDatePicker.endDate,
            onDateSelected = {},
            onDismiss = {},
            onJumpToToday = {},
        )
    }
}
