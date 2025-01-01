package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.calendar.component.landscape.CalendarDayLandscape
import com.lukeneedham.videodiary.ui.feature.calendar.component.landscape.CalendarScrollerLandscape
import com.lukeneedham.videodiary.ui.feature.calendar.component.portrait.CalendarDayPortrait
import com.lukeneedham.videodiary.ui.feature.calendar.component.portrait.CalendarScrollerPortrait
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
    share: (ShareRequest) -> Unit,
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

    val currentDateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    val currentDateFormatted = currentDay.date.format(currentDateFormatter)

    val onPrevious = {
        goToPage(currentDayIndex - 1)
    }
    val onNext = {
        goToPage(currentDayIndex + 1)
    }

    @Composable
    fun DayContentFrame(
        content: @Composable () -> Unit
    ) {
        key(currentDay) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                content()
            }
        }
    }

    BoxWithConstraints {
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val isPortrait = height > width

        if (isPortrait) {
            CalendarScrollerPortrait(
                exportFullVideo = exportFullVideo,
                onPrevious = onPrevious,
                onNext = onNext,
                openDayPicker = openDayPicker,
                currentDateFormatted = currentDateFormatted,
            ) {
                DayContentFrame {
                    CalendarDayPortrait(
                        day = currentDay,
                        videoAspectRatio = videoAspectRatio,
                        onRecordTodayVideoClick = onRecordTodayVideoClick,
                        onDeleteTodayVideoClick = onDeleteTodayVideoClick,
                        videoPlayerController = videoPlayerController,
                        share = share,
                    )
                }
            }
        } else {
            CalendarScrollerLandscape(
                exportFullVideo = exportFullVideo,
                onPrevious = onPrevious,
                onNext = onNext,
                openDayPicker = openDayPicker,
                currentDateFormatted = currentDateFormatted,
            ) {
                DayContentFrame {
                    CalendarDayLandscape(
                        day = currentDay,
                        videoAspectRatio = videoAspectRatio,
                        onRecordTodayVideoClick = onRecordTodayVideoClick,
                        onDeleteTodayVideoClick = onDeleteTodayVideoClick,
                        videoPlayerController = videoPlayerController,
                        share = share,
                    )
                }
            }
        }
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
        share = {},
    )
}