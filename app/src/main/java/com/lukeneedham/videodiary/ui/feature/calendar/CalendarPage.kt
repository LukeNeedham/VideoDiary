package com.lukeneedham.videodiary.ui.feature.calendar

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.ShareRequest
import java.time.LocalDate

@Composable
fun CalendarPage(
    viewModel: CalendarViewModel,
    onRecordVideoClick: (date: LocalDate) -> Unit,
    onExportClick: () -> Unit,
    onDebugClick: () -> Unit,
    share: (ShareRequest) -> Unit,
) {
    CalendarPageContent(
        days = viewModel.days,
        videoAspectRatio = viewModel.videoAspectRatio,
        currentDayIndex = viewModel.currentDayIndex,
        allowEditPastDays = viewModel.allowEditPastDays,
        onRecordVideoClick = onRecordVideoClick,
        onDeleteVideoClick = viewModel::deleteVideo,
        goToDate = viewModel::goToDate,
        setCurrentDayIndex = viewModel::setCurrentDay,
        share = share,
        videoPlayerController = viewModel.videoPlayerController,
        onExportClick = onExportClick,
        onDebugClick = onDebugClick,
    )
}