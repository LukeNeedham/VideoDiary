package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.lukeneedham.videodiary.R
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.theme.AppTheme
import java.time.LocalDate

private val CalendarSideMargin = 20.dp

/**
 * What the date picker sheet shows: the title of the visible month, the jump-to-today button (when [onJumpToToday] is set)
 * and the calendar, or a placeholder until the [videoAspectRatio] is known.
 */
@Composable
internal fun DiaryDatePickerSheetContent(
    /** All days of the diary, grouped by calendar month in chronological order */
    months: List<List<Day>>,
    /** Null until it is known: a placeholder shows meanwhile */
    videoAspectRatio: Float?,
    initialFocusedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onJumpToToday: (() -> Unit)? = null,
    /** If set, days before this date are shown but can't be selected */
    minDate: LocalDate? = null,
) {
    // Only read inside MonthTitle, so that swiping between months doesn't recompose the whole sheet
    val monthTitle = remember { mutableStateOf("") }

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
                    .clickable { onJumpToToday() }
                    .padding(8.dp)
            )
        }
    }
    if (videoAspectRatio != null) {
        DiaryDatePicker(
            initialFocusedDate = initialFocusedDate,
            allMonths = months,
            videoAspectRatio = videoAspectRatio,
            onDateSelected = { onDateSelected(it) },
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

@Preview
@Composable
private fun Preview() {
    AppTheme {
        DiaryDatePickerSheetContent(
            months = MockDataDiaryDatePicker.months,
            videoAspectRatio = MockDataDiaryDatePicker.videoAspectRatio,
            initialFocusedDate = MockDataDiaryDatePicker.endDate,
            onDateSelected = {},
            onJumpToToday = {},
        )
    }
}
