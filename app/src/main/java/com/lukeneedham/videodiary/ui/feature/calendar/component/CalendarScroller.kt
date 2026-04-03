package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.calendar.component.landscape.CalendarDayLandscape
import com.lukeneedham.videodiary.ui.feature.calendar.component.landscape.CalendarScrollerLandscape
import com.lukeneedham.videodiary.ui.feature.calendar.component.portrait.CalendarDayPortrait
import com.lukeneedham.videodiary.ui.feature.calendar.component.portrait.CalendarScrollerPortrait
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import kotlinx.coroutines.launch

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
    videoPlayerController: VideoPlayerController,
) {
    val pagerState = rememberPagerState(
        initialPage = currentDayIndex,
        pageCount = { days.size },
    )
    val coroutineScope = rememberCoroutineScope()

    // Notify the ViewModel when the pager settles on a new page.
    LaunchedEffect(pagerState.currentPage) {
        setCurrentDayIndex(pagerState.currentPage)
    }

    // Respond to external navigation (e.g. date picker) by scrolling the pager.
    LaunchedEffect(currentDayIndex) {
        if (pagerState.currentPage != currentDayIndex) {
            pagerState.scrollToPage(currentDayIndex)
        }
    }

    // Pause all videos while the pager is being dragged; resume once it settles.
    LaunchedEffect(pagerState.isScrollInProgress) {
        if (pagerState.isScrollInProgress) {
            videoPlayerController.temporaryPause()
        } else {
            videoPlayerController.temporaryResume()
        }
    }

    val currentDay = days[pagerState.currentPage]
    val currentDateFormatted = currentDay.date.format(StandardDateTimeFormatter.date)

    val onPrevious = {
        coroutineScope.launch {
            val target = pagerState.currentPage - 1
            if (target in days.indices) {
                pagerState.animateScrollToPage(target)
            }
        }
        Unit
    }
    val onNext = {
        coroutineScope.launch {
            val target = pagerState.currentPage + 1
            if (target in days.indices) {
                pagerState.animateScrollToPage(target)
            }
        }
        Unit
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
                HorizontalPager(
                    state = pagerState,
                    beyondBoundsPageCount = 0,
                    modifier = Modifier.fillMaxSize(),
                ) { pageIndex ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                    ) {
                        CalendarDayPortrait(
                            day = days[pageIndex],
                            videoAspectRatio = videoAspectRatio,
                            onRecordTodayVideoClick = onRecordTodayVideoClick,
                            onDeleteTodayVideoClick = onDeleteTodayVideoClick,
                            videoPlayerController = videoPlayerController,
                            share = share,
                        )
                    }
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
                HorizontalPager(
                    state = pagerState,
                    beyondBoundsPageCount = 0,
                    modifier = Modifier.fillMaxSize(),
                ) { pageIndex ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                    ) {
                        CalendarDayLandscape(
                            day = days[pageIndex],
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
        videoPlayerController = rememberVideoPlayerController(),
    )
}