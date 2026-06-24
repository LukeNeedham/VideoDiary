package com.lukeneedham.videodiary.ui.feature.calendar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.util.logger.Logger
import com.lukeneedham.videodiary.ui.feature.calendar.component.CalendarDeleteConfirmDialog
import com.lukeneedham.videodiary.ui.feature.calendar.component.CalendarScroller
import com.lukeneedham.videodiary.ui.feature.calendar.component.CalendarSideMenu
import com.lukeneedham.videodiary.ui.feature.common.datepicker.DiaryDatePickerDialog
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import com.lukeneedham.videodiary.ui.theme.AppBackground
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun CalendarPageContent(
    days: List<Day>,
    videoAspectRatio: Float?,
    currentDayIndex: Int,
    allowEditPastDays: Boolean,
    onRecordVideoClick: (date: LocalDate) -> Unit,
    onDeleteVideoClick: (date: LocalDate) -> Unit,
    goToDate: (date: LocalDate) -> Unit,
    setCurrentDayIndex: (Int) -> Unit,
    share: (ShareRequest) -> Unit,
    videoPlayerController: VideoPlayerController,
    onExportClick: () -> Unit,
    onExportCatalogueClick: () -> Unit,
    onDebugClick: () -> Unit,
) {
    val currentDay = days[currentDayIndex]

    var showDayPickerDialog by remember { mutableStateOf(false) }
    var pendingDateToDelete: LocalDate? by remember { mutableStateOf(null) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch { drawerState.close() }
    }

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            CalendarSideMenu(
                onExportClick = {
                    coroutineScope.launch { drawerState.close() }
                    onExportClick()
                },
                onExportCatalogueClick = {
                    coroutineScope.launch { drawerState.close() }
                    onExportCatalogueClick()
                },
                onDebugClick = {
                    coroutineScope.launch { drawerState.close() }
                    onDebugClick()
                },
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
        ) {
            if (days.isNotEmpty() && videoAspectRatio != null) {
                CalendarScroller(
                    days = days,
                    videoAspectRatio = videoAspectRatio,
                    allowEditPastDays = allowEditPastDays,
                    onRecordVideoClick = onRecordVideoClick,
                    onDeleteVideoClick = {
                        pendingDateToDelete = it
                    },
                    openDayPicker = {
                        showDayPickerDialog = true
                    },
                    currentDayIndex = currentDayIndex,
                    setCurrentDayIndex = setCurrentDayIndex,
                    goToToday = { goToDate(LocalDate.now()) },
                    onMenuClick = {
                        coroutineScope.launch { drawerState.open() }
                    },
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

            val dateToDelete = pendingDateToDelete
            if (dateToDelete != null) {
                CalendarDeleteConfirmDialog(
                    dismiss = {
                        pendingDateToDelete = null
                    },
                    onConfirm = {
                        onDeleteVideoClick(dateToDelete)
                    },
                )
            }
        }
    }
}

@Composable
private fun Preview(
    currentDayIndex: Int,
) {
    Box(
        modifier = Modifier.background(AppBackground)
    ) {
        CalendarPageContent(
            days = MockDataCalendar.days,
            videoAspectRatio = MockDataCalendar.videoAspectRatio,
            currentDayIndex = currentDayIndex,
            allowEditPastDays = false,
            onRecordVideoClick = {},
            onDeleteVideoClick = {},
            goToDate = {},
            setCurrentDayIndex = {},
            share = {},
            videoPlayerController = rememberVideoPlayerController(),
            onExportClick = {},
            onExportCatalogueClick = {},
            onDebugClick = {},
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