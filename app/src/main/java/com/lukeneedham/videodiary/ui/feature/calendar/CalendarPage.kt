package com.lukeneedham.videodiary.ui.feature.calendar

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.ShareRequest
import java.time.LocalDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CalendarPage(
    onRecordVideoClick: (date: LocalDate) -> Unit,
    onMenuClick: () -> Unit,
    share: (ShareRequest) -> Unit,
) {
    val viewModel = koinViewModel<CalendarViewModel>()

    CalendarPageContent(
        days = viewModel.days,
        currentDayIndex = viewModel.currentDayIndex,
        allowEditPastDays = viewModel.allowEditPastDays,
        onRecordVideoClick = onRecordVideoClick,
        onDeleteVideoClick = viewModel::deleteVideo,
        goToDate = viewModel::goToDate,
        setCurrentDayIndex = viewModel::setCurrentDay,
        share = share,
        videoPlayerController = viewModel.videoPlayerController,
        onMenuClick = onMenuClick,
    )
}
