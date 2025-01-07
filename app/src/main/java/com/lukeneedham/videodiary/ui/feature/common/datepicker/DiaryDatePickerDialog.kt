package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryDatePickerDialog(
    firstPossibleDate: LocalDate,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit,
    onDismiss: () -> Unit,
) {
    val zone = ZoneId.systemDefault()
    fun toMillis(date: LocalDate) =
        date.atStartOfDay(zone).toInstant().toEpochMilli()

    fun toDate(millis: Long) =
        LocalDate.ofInstant(Instant.ofEpochMilli(millis), zone)

    val today = LocalDate.now()
    val selectableDates = remember(firstPossibleDate, today) {
        val todayMillis = toMillis(today)
        val diaryStartDateMillis = toMillis(firstPossibleDate)

        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long) =
                utcTimeMillis in diaryStartDateMillis..todayMillis
        }
    }
    val selectedDateMillis = if (selectedDate == null) {
        null
    } else {
        toMillis(selectedDate)
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis,
        selectableDates = selectableDates,
    )

    DiaryDatePickerDialogScaffold(
        onDismiss = onDismiss,
        onAccept = {
            val millis = datePickerState.selectedDateMillis
            val date = if (millis == null) null else toDate(millis)
            onDateSelected(date)
            onDismiss()
        }
    ) {
        DatePicker(
            state = datePickerState,
        )
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayPicker() {
    DiaryDatePickerDialog(
        firstPossibleDate = MockDataCalendar.startDate,
        onDateSelected = {},
        onDismiss = {},
        selectedDate = MockDataCalendar.day.date,
    )
}