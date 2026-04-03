package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.Day
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun DiaryDatePicker(
    initialFocusedDate: LocalDate,
    weeks: List<List<Day>>,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val weekDays = weeks.map { week ->
        val weekStartsOn = DayOfWeek.MONDAY
        val weekEndsOn = DayOfWeek.SUNDAY

        val missingDaysStart = week.first().date.dayOfWeek.value - weekStartsOn.value
        val missingDaysEnd = weekEndsOn.value - week.last().date.dayOfWeek.value

        val placeholderDaysStart = List(missingDaysStart) { Weekday.Empty }
        val placeholderDaysEnd = List(missingDaysEnd) { Weekday.Empty }

        placeholderDaysStart + week.map { Weekday.Value(it) } + placeholderDaysEnd
    }

    val today = LocalDate.now()
    val rows = buildList<WeekRow> {
        var previousWasCollapsed = false
        weekDays.forEach { week ->
            val isCurrentWeek = week.any { it is Weekday.Value && it.day.date == today }
            val isEmpty = week.none { it is Weekday.Value && it.day.video != null }
            if (!isCurrentWeek && isEmpty) {
                if (!previousWasCollapsed) {
                    add(WeekRow.Collapsed)
                    previousWasCollapsed = true
                }
            } else {
                add(WeekRow.Normal(week))
                previousWasCollapsed = false
            }
        }
    }

    val firstVisibleWeekIndex = remember {
        val res = rows.indexOfFirst { row ->
            row is WeekRow.Normal && row.week.any { it is Weekday.Value && it.day.date == initialFocusedDate }
        }
        if (res == -1) 0 else res
    }

    val state = rememberLazyListState(
        initialFirstVisibleItemIndex = firstVisibleWeekIndex,
    )

    LaunchedEffect(firstVisibleWeekIndex) {
        state.scrollToItem(firstVisibleWeekIndex)
    }

    LazyColumn(
        state = state,
        modifier = modifier
    ) {
        items(rows) { row ->
            when (row) {
                is WeekRow.Normal -> {
                    Row(modifier = Modifier.fillParentMaxWidth()) {
                        row.week.forEach { weekday ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                when (weekday) {
                                    is Weekday.Empty -> {
                                        // Nothing
                                    }

                                    is Weekday.Value -> {
                                        val day = weekday.day
                                        DiaryDatePickerDay(
                                            day = day,
                                            onClick = {
                                                onDateSelected(day.date)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                is WeekRow.Collapsed -> {
                    val collapsedDescription = stringResource(R.string.date_picker_collapsed_weeks_description)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .semantics { contentDescription = collapsedDescription }
                    ) {
                        Text(text = stringResource(R.string.date_picker_collapsed_weeks))
                    }
                }
            }
        }
    }
}

private sealed interface Weekday {
    data class Value(val day: Day) : Weekday
    data object Empty : Weekday
}

private sealed interface WeekRow {
    data class Normal(val week: List<Weekday>) : WeekRow
    data object Collapsed : WeekRow
}

@Preview
@Composable
private fun Preview() {
    DiaryDatePicker(
        initialFocusedDate = MockDataDiaryDatePicker.endDate,
        weeks = MockDataDiaryDatePicker.weeks,
        onDateSelected = {},
    )
}