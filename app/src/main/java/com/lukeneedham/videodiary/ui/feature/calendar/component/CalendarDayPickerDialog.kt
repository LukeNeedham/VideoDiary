package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDayPickerDialog(
    startDate: LocalDate,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    val zone = ZoneId.systemDefault()
    fun toMillis(date: LocalDate) =
        date.atStartOfDay(zone).toInstant().toEpochMilli()

    fun toDate(millis: Long) =
        LocalDate.ofInstant(Instant.ofEpochMilli(millis), zone)

    val today = LocalDate.now()
    val selectableDates = remember(startDate, today) {
        val todayMillis = toMillis(today)
        val startDateMillis = toMillis(startDate)

        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long) =
                utcTimeMillis in startDateMillis..todayMillis
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

    @Composable
    fun Button(
        text: String,
        onClick: () -> Unit,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .heightIn(min = 50.dp)
                .widthIn(min = 50.dp)
                .clickable {
                    onClick()
                }
        ) {
            Text(
                text = text,
                color = Color.Black,
            )
        }
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                text = "Ok",
                onClick = {
                    val millis = datePickerState.selectedDateMillis
                    val date = if (millis == null) null else toDate(millis)
                    onDateSelected(date)
                    onDismiss()
                }
            )
        },
        dismissButton = {
            Button(
                text = "Cancel",
                onClick = {
                    onDismiss()
                }
            )
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayPicker() {
    CalendarDayPickerDialog(
        startDate = MockDataCalendar.startDate,
        onDateSelected = {},
        onDismiss = {},
        selectedDate = MockDataCalendar.day.date,
    )
}