package com.lukeneedham.videodiary.ui.feature.calendar

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.ShareRequest
import java.time.LocalDate

@Composable
fun CalendarPage(
    viewModel: CalendarViewModel,
    onRecordVideoClick: (date: LocalDate) -> Unit,
    share: (ShareRequest) -> Unit,
) {
    CalendarPageContent(
        days = viewModel.days,
        videoAspectRatio = viewModel.videoAspectRatio,
        currentDayIndex = viewModel.currentDayIndex,
        allowRetakeForPastDays = viewModel.allowRetakeForPastDays,
        onRecordVideoClick = onRecordVideoClick,
        onDeleteTodayVideoClick = viewModel::deleteTodayVideo,
        goToDate = viewModel::goToDate,
        setCurrentDayIndex = viewModel::setCurrentDay,
        share = share,
        videoPlayerController = viewModel.videoPlayerController,
    )
}