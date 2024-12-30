package com.lukeneedham.videodiary.ui.feature.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.calendar.component.CalendarDayPickerDialog
import com.lukeneedham.videodiary.ui.feature.calendar.component.CalendarScroller
import java.time.LocalDate

@Composable
fun CalendarPageContent(
    days: List<Day>,
    videoAspectRatio: Float?,
    startDate: LocalDate?,
    currentDayIndex: Int,
    onRecordTodayVideoClick: () -> Unit,
    onDeleteTodayVideoClick: () -> Unit,
    goToDate: (date: LocalDate) -> Unit,
    setCurrentDayIndex: (Int) -> Unit,
    exportFullVideo: () -> Unit,
    share: (ShareRequest) -> Unit,
) {
    val currentDay = days.getOrNull(currentDayIndex)

    var showDayPickerDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (days.isNotEmpty() && videoAspectRatio != null) {
            CalendarScroller(
                days = days,
                videoAspectRatio = videoAspectRatio,
                onRecordTodayVideoClick = onRecordTodayVideoClick,
                onDeleteTodayVideoClick = onDeleteTodayVideoClick,
                openDayPicker = {
                    showDayPickerDialog = true
                },
                currentDayIndex = currentDayIndex,
                setCurrentDayIndex = setCurrentDayIndex,
                exportFullVideo = exportFullVideo,
                share = share,
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        if (startDate != null && showDayPickerDialog) {
            CalendarDayPickerDialog(
                startDate = startDate,
                selectedDate = currentDay?.date,
                onDateSelected = { date ->
                    if (date != null) {
                        goToDate(date)
                    }
                },
                onDismiss = {
                    showDayPickerDialog = false
                }
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarPageContent() {
    CalendarPageContent(
        days = MockDataCalendar.days,
        videoAspectRatio = MockDataCalendar.videoAspectRatio,
        onRecordTodayVideoClick = {},
        onDeleteTodayVideoClick = {},
        goToDate = {},
        startDate = MockDataCalendar.startDate,
        currentDayIndex = 0,
        setCurrentDayIndex = {},
        exportFullVideo = {},
        share = {},
    )
}