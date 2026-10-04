package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.theme.Typography
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

private const val DAYS_PER_WEEK = 7

/**
 * A traditional calendar: one page per month, with 7 days per row, and horizontal swiping between months.
 *
 * @param months each item is all of the [Day]s in one calendar month, ordered chronologically
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiaryDatePicker(
    initialFocusedDate: LocalDate,
    months: List<List<Day>>,
    videoAspectRatio: Float,
    onDateSelected: (LocalDate) -> Unit,
    onVisibleMonthChanged: (monthName: String, year: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (months.isEmpty()) return

    val initialPage = remember {
        val index = months.indexOfFirst { month ->
            val date = month.first().date
            date.year == initialFocusedDate.year && date.month == initialFocusedDate.month
        }
        if (index == -1) months.lastIndex else index
    }
    val pagerState = rememberPagerState(initialPage = initialPage) { months.size }

    LaunchedEffect(months) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                val date = months.getOrNull(page)?.firstOrNull()?.date ?: return@collect
                val monthName = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                onVisibleMonthChanged(monthName, date.year.toString())
            }
    }

    Column(modifier = modifier) {
        WeekdayHeader(modifier = Modifier.padding(horizontal = 2.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f, fill = false)
        ) { page ->
            MonthGrid(
                days = months[page],
                videoAspectRatio = videoAspectRatio,
                onDateSelected = onDateSelected,
            )
        }
    }
}

@Composable
private fun WeekdayHeader(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        DayOfWeek.values().forEach { dayOfWeek ->
            Text(
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = Color.Black.copy(alpha = 0.6f),
                fontSize = Typography.Size.extraSmall,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MonthGrid(
    days: List<Day>,
    videoAspectRatio: Float,
    onDateSelected: (LocalDate) -> Unit,
) {
    // Weeks start on Monday
    val leadingPlaceholders = days.first().date.dayOfWeek.value - DayOfWeek.MONDAY.value
    val cells: List<Day?> = List(leadingPlaceholders) { null } + days
    val rows = cells.chunked(DAYS_PER_WEEK)

    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 2.dp)
    ) {
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(DAYS_PER_WEEK) { index ->
                    val day = row.getOrNull(index)
                    Box(modifier = Modifier.weight(1f)) {
                        if (day != null) {
                            DiaryDatePickerDay(
                                day = day,
                                videoAspectRatio = videoAspectRatio,
                                onClick = { onDateSelected(day.date) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    DiaryDatePicker(
        initialFocusedDate = MockDataDiaryDatePicker.endDate,
        months = MockDataDiaryDatePicker.months,
        videoAspectRatio = MockDataDiaryDatePicker.videoAspectRatio,
        onDateSelected = {},
        onVisibleMonthChanged = { _, _ -> },
    )
}
