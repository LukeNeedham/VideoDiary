package com.lukeneedham.videodiary.ui.feature.calendar

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.ShareRequest

@Composable
fun CalendarPage(
    viewModel: CalendarViewModel,
    onRecordTodayVideoClick: () -> Unit,
    exportFullVideo: () -> Unit,
    share: (ShareRequest) -> Unit,
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
        share = share,
        videoPlayerController = viewModel.videoPlayerController,
    )
}