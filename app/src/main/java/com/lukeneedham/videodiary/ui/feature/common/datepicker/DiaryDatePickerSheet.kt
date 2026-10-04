package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetDefaults
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetLayout
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.time.LocalDate

private val CalendarSideMargin = 20.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryDatePickerSheet(
    initialFocusedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    onJumpToToday: (() -> Unit)? = null,
    /** If set, days before this date are shown but can't be selected */
    minDate: LocalDate? = null,
    viewModel: DiaryDatePickerViewModel = koinInject(),
) {
    DisposableEffect(viewModel) {
        onDispose {
            viewModel.dispose()
        }
    }

    // Only read inside MonthTitle, so that swiping between months doesn't recompose the whole sheet
    val monthTitle = remember { mutableStateOf("") }

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
        windowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        SheetLayout(onClose = { hideThen {} }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            MonthTitle(
                title = { monthTitle.value },
                modifier = Modifier.align(Alignment.Center)
            )
            if (onJumpToToday != null) {
                Icon(
                    painter = painterResource(R.drawable.calendar_today),
                    contentDescription = "Jump to today",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { hideThen(onJumpToToday) }
                        .padding(8.dp)
                )
            }
        }
        val videoAspectRatio = viewModel.videoAspectRatio
        if (videoAspectRatio != null) {
            DiaryDatePicker(
                initialFocusedDate = initialFocusedDate,
                allMonths = viewModel.months,
                videoAspectRatio = videoAspectRatio,
                onDateSelected = { hideThen { onDateSelected(it) } },
                minDate = minDate,
                onVisibleMonthChanged = { monthName, year ->
                    monthTitle.value = "$monthName $year"
                },
                // Side margins narrow the cells, which also makes the (aspect ratio sized) sheet shorter
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = CalendarSideMargin, end = CalendarSideMargin, bottom = 8.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(200.dp))
        }
        }
    }
}

@Composable
private fun MonthTitle(title: () -> String, modifier: Modifier = Modifier) {
    Text(
        text = title(),
        color = Color.White,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

