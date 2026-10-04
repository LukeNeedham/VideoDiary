package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.unit.sp
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.theme.Typography
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private const val DAYS_PER_WEEK = 7

/** The most rows a month can span when weeks start on Monday */
private const val MAX_WEEKS_PER_MONTH = 6

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
    val daysByDate = remember(months) { months.flatten().associateBy { it.date } }
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
                month = YearMonth.from(months[page].first().date),
                daysByDate = daysByDate,
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
                color = Color.White.copy(alpha = 0.6f),
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
    month: YearMonth,
    daysByDate: Map<LocalDate, Day>,
    videoAspectRatio: Float,
    onDateSelected: (LocalDate) -> Unit,
) {
    val today = LocalDate.now()
    // Weeks start on Monday
    val leadingDays = month.atDay(1).dayOfWeek.value - DayOfWeek.MONDAY.value
    val gridStart = month.atDay(1).minusDays(leadingDays.toLong())

    // Always show the worst case number of rows, so that every month has the same height
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 2.dp)
    ) {
        repeat(MAX_WEEKS_PER_MONTH) { weekIndex ->
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(DAYS_PER_WEEK) { dayIndex ->
                    val date = gridStart.plusDays((weekIndex * DAYS_PER_WEEK + dayIndex).toLong())
                    val isInMonth = YearMonth.from(date) == month
                    val day = daysByDate[date]
                    Box(modifier = Modifier.weight(1f)) {
                        when {
                            date.isAfter(today) -> {
                                // Never show the future. Keep the cell's size so the row height is stable.
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(videoAspectRatio)
                                )
                            }

                            day != null -> {
                                DiaryDatePickerDay(
                                    day = day,
                                    videoAspectRatio = videoAspectRatio,
                                    dimmed = !isInMonth,
                                    onClick = { onDateSelected(date) },
                                )
                            }

                            else -> {
                                // Before the diary began
                                Box(
                                    contentAlignment = Alignment.BottomCenter,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(videoAspectRatio)
                                ) {
                                    Text(
                                        text = date.dayOfMonth.toString(),
                                        color = Color.White.copy(alpha = 0.3f),
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(vertical = 1.dp)
                                    )
                                }
                            }
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
