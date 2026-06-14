package com.lukeneedham.videodiary.ui.feature.calendar.component.landscape

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.calendar.component.CalendarDaySelector

@Composable
fun CalendarScrollerLandscape(
    exportFullVideo: () -> Unit,
    onDebugClick: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    openDayPicker: () -> Unit,
    currentDateFormatted: String,
    dayContent: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        dayContent()

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .align(Alignment.TopStart)
                .safeDrawingPadding()
                .padding(12.dp)
        ) {
            CalendarToolbarLandscape(
                exportFullVideo = exportFullVideo,
                onDebugClick = onDebugClick,
            )
            CalendarDaySelector(
                currentDate = currentDateFormatted,
                onPrevious = onPrevious,
                onNext = onNext,
                openDayPicker = openDayPicker,
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarScrollerLandscape() {
    CalendarScrollerLandscape(
        exportFullVideo = {},
        onDebugClick = {},
        onPrevious = {},
        onNext = {},
        openDayPicker = {},
        currentDateFormatted = "aaa",
        dayContent = {
            Text("aaaa")
        },
    )
}
