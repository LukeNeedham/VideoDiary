package com.lukeneedham.videodiary.ui.feature.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.calendar.component.CalendarDeleteConfirmDialog
import com.lukeneedham.videodiary.ui.feature.calendar.component.CalendarScroller
import com.lukeneedham.videodiary.ui.feature.common.datepicker.DiaryDatePickerDialog
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import java.time.LocalDate

@Composable
fun CalendarPageContent(
    days: List<Day>,
    videoAspectRatio: Float?,
    currentDayIndex: Int,
    onRecordTodayVideoClick: () -> Unit,
    onDeleteTodayVideoClick: () -> Unit,
    goToDate: (date: LocalDate) -> Unit,
    setCurrentDayIndex: (Int) -> Unit,
    exportFullVideo: () -> Unit,
    share: (ShareRequest) -> Unit,
    videoPlayerController: VideoPlayerController,
) {
    val currentDay = days[currentDayIndex]

    var showDayPickerDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by rememberSaveable { mutableStateOf(false) }

    val onDeleteClick = {
        showDeleteConfirmDialog = true
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (days.isNotEmpty() && videoAspectRatio != null) {
            CalendarScroller(
                days = days,
                videoAspectRatio = videoAspectRatio,
                onRecordTodayVideoClick = onRecordTodayVideoClick,
                onDeleteTodayVideoClick = onDeleteClick,
                openDayPicker = {
                    showDayPickerDialog = true
                },
                currentDayIndex = currentDayIndex,
                setCurrentDayIndex = setCurrentDayIndex,
                exportFullVideo = exportFullVideo,
                share = share,
                videoPlayerController = videoPlayerController,
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

        if (showDayPickerDialog) {
            DiaryDatePickerDialog(
                initialFocusedDate = currentDay.date,
                onDateSelected = { date ->
                    goToDate(date)
                },
                onDismiss = {
                    showDayPickerDialog = false
                }
            )
        }

        if (showDeleteConfirmDialog) {
            CalendarDeleteConfirmDialog(
                dismiss = {
                    showDeleteConfirmDialog = false
                },
                onConfirm = onDeleteTodayVideoClick,
            )
        }
    }
}

@Composable
private fun Preview(
    currentDayIndex: Int,
) {
    Box(
        modifier = Modifier.background(Color.White)
    ) {
        CalendarPageContent(
            days = MockDataCalendar.days,
            videoAspectRatio = MockDataCalendar.videoAspectRatio,
            currentDayIndex = currentDayIndex,
            onRecordTodayVideoClick = {},
            onDeleteTodayVideoClick = {},
            goToDate = {},
            setCurrentDayIndex = {},
            exportFullVideo = {},
            share = {},
            videoPlayerController = rememberVideoPlayerController(),
        )
    }
}

@Preview
@Composable
private fun PreviewDay1() {
    Preview(currentDayIndex = 0)
}

@Preview
@Composable
private fun PreviewDay2() {
    Preview(currentDayIndex = 1)
}