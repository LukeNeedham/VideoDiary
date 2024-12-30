package com.lukeneedham.videodiary.ui.feature.calendar

import androidx.compose.runtime.Composable

@Composable
fun CalendarPage(
    viewModel: CalendarViewModel,
    onRecordTodayVideoClick: () -> Unit,
    exportFullVideo: () -> Unit,
) {
    CalendarPageContent(
        days = viewModel.days,
        videoAspectRatio = viewModel.videoAspectRatio,
        onRecordTodayVideoClick = onRecordTodayVideoClick,
        onDeleteTodayVideoClick = viewModel::deleteTodayVideo,
        startDate = viewModel.startDate,
        currentDayIndex = viewModel.currentDayIndex,
        setCurrentDayIndex = viewModel::setCurrentDay,
        goToDate = viewModel::goToDate,
        exportFullVideo = exportFullVideo,
    )
}