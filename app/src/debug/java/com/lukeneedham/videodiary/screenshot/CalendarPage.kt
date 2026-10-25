package com.lukeneedham.videodiary.screenshot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.feature.calendar.CalendarPageContent
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import com.lukeneedham.videodiary.ui.theme.AppBackground
import java.io.File
import java.time.LocalDate

/** Rendered by CI to docs/screenshots/<position in order.txt>-CalendarPage.png: the calendar on a day with a video. */
@Preview
@Composable
private fun CalendarPageScreenshot() {
    Box(modifier = Modifier.background(AppBackground)) {
        CalendarPageContent(
            days = listOf(
                Day(date = LocalDate.of(2024, 12, 26), videoFile = File("")),
                Day(date = LocalDate.of(2024, 12, 27), videoFile = File("")),
            ),
            currentDayIndex = 1,
            allowEditPastDays = false,
            onRecordVideoClick = {},
            onDeleteVideoClick = {},
            goToDate = {},
            setCurrentDayIndex = {},
            share = {},
            videoPlayerController = rememberVideoPlayerController(),
            onMenuClick = {},
        )
    }
}
