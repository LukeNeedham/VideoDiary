package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScroller(
    days: List<Day>,
    currentDayIndex: Int,
    videoAspectRatio: Float,
    onRecordTodayVideoClick: () -> Unit,
    onDeleteTodayVideoClick: () -> Unit,
    openDayPicker: () -> Unit,
    exportFullVideo: () -> Unit,
    setCurrentDayIndex: (Int) -> Unit,
) {
    val currentDay = days[currentDayIndex]

    /**
     * Video Player Controller, shared across all day videos.
     * This allows settings (like muted state) to persist across days.
     */
    val videoPlayerController = rememberVideoPlayerController()

    fun goToPage(index: Int) {
        if (index !in days.indices) return
        setCurrentDayIndex(index)
    }

    Column {
        val currentDateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val currentDateFormatted = currentDay.date.format(currentDateFormatter)

        Spacer(modifier = Modifier.height(20.dp))
        CalendarDaySelector(
            currentDate = currentDateFormatted,
            onPrevious = {
                goToPage(currentDayIndex - 1)
            },
            onNext = {
                goToPage(currentDayIndex + 1)
            },
            openDayPicker = openDayPicker,
        )
        Spacer(modifier = Modifier.height(10.dp))

        key(currentDay) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                CalendarDay(
                    day = currentDay,
                    videoAspectRatio = videoAspectRatio,
                    onRecordTodayVideoClick = onRecordTodayVideoClick,
                    onDeleteTodayVideoClick = onDeleteTodayVideoClick,
                    videoPlayerController = videoPlayerController,
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Button(
            text = "Export full video",
            onClick = exportFullVideo,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview
@Composable
internal fun PreviewCalendarScroller() {
    CalendarScroller(
        days = MockDataCalendar.days,
        videoAspectRatio = 1f,
        onRecordTodayVideoClick = {},
        onDeleteTodayVideoClick = {},
        openDayPicker = {},
        setCurrentDayIndex = {},
        exportFullVideo = {},
        currentDayIndex = 0,
    )
}