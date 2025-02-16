package com.lukeneedham.videodiary.ui.feature.calendar.component.landscape

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.calendar.component.CalendarDaySelector

@Composable
fun CalendarScrollerLandscape(
    exportFullVideo: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    openDayPicker: () -> Unit,
    currentDateFormatted: String,
    dayContent: @Composable () -> Unit
) {
    Row {
        CalendarToolbarLandscape(
            exportFullVideo = exportFullVideo,
        )

        Column {
            Spacer(modifier = Modifier.height(2.dp))
            CalendarDaySelector(
                currentDate = currentDateFormatted,
                onPrevious = onPrevious,
                onNext = onNext,
                openDayPicker = openDayPicker,
            )
            Spacer(modifier = Modifier.height(2.dp))

            Box(modifier = Modifier.weight(1f)) {
                dayContent()
            }

            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarScrollerLandscape() {
    CalendarScrollerLandscape(
        exportFullVideo = {},
        onPrevious = {},
        onNext = {},
        openDayPicker = {},
        currentDateFormatted = "aaa",
        dayContent = {
            Text("aaaa")
        },
    )
}