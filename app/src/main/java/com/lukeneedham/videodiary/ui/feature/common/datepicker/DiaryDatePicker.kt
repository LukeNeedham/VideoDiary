package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.theme.Typography
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
        weekDays.forEach { week ->
            val isCurrentWeek = week.any { it is Weekday.Value && it.day.date == today }
            val isEmpty = week.none { it is Weekday.Value && it.day.videoFile != null }
            if (!isCurrentWeek && isEmpty) {
                val previous = lastOrNull()
                if (previous is WeekRow.Collapsed) {
                    this[lastIndex] = WeekRow.Collapsed(previous.weekCount + 1)
                } else {
                    add(WeekRow.Collapsed(weekCount = 1))
                }
            } else {
                add(WeekRow.Normal(week))
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
                    val weekCount = row.weekCount
                    val collapsedDescription = pluralStringResource(
                        R.plurals.date_picker_collapsed_weeks_description,
                        weekCount,
                        weekCount,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(vertical = 6.dp)
                            .semantics { contentDescription = collapsedDescription }
                    ) {
                        CollapsedWeeksEllipsis()
                        Text(
                            text = pluralStringResource(
                                R.plurals.date_picker_collapsed_weeks_count,
                                weekCount,
                                weekCount,
                            ),
                            color = Color.Black.copy(alpha = 0.4f),
                            fontSize = Typography.Size.extraSmall,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CollapsedWeeksEllipsis() {
    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(color = Color.Black.copy(alpha = 0.4f), shape = CircleShape)
            )
        }
    }
}

private sealed interface Weekday {
    data class Value(val day: Day) : Weekday
    data object Empty : Weekday
}

private sealed interface WeekRow {
    data class Normal(val week: List<Weekday>) : WeekRow
    data class Collapsed(val weekCount: Int) : WeekRow
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