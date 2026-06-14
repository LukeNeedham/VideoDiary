package com.lukeneedham.videodiary.ui.feature.calendar.component.portrait

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.calendar.component.CalendarDaySelector
import com.lukeneedham.videodiary.ui.feature.common.glass.TopScrim

@Composable
fun CalendarScrollerPortrait(
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

        TopScrim(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .height(170.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .safeDrawingPadding()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            CalendarToolbarPortrait(
                exportFullVideo = exportFullVideo,
                onDebugClick = onDebugClick,
            )
            CalendarDaySelector(
                currentDate = currentDateFormatted,
                onPrevious = onPrevious,
                onNext = onNext,
                openDayPicker = openDayPicker,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarScrollerPortrait() {
    CalendarScrollerPortrait(
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
