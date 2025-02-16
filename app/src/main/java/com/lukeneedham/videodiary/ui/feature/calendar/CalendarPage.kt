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
        currentDayIndex = viewModel.currentDayIndex,
        onRecordTodayVideoClick = onRecordTodayVideoClick,
        onDeleteTodayVideoClick = viewModel::deleteTodayVideo,
        goToDate = viewModel::goToDate,
        setCurrentDayIndex = viewModel::setCurrentDay,
        exportFullVideo = exportFullVideo,
        share = share,
        videoPlayerController = viewModel.videoPlayerController,
    )
}