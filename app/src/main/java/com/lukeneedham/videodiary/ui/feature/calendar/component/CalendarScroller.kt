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
import androidx.compose.runtime.snapshotFlow
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
import kotlinx.coroutines.flow.distinctUntilChanged
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
        initialPage = if (days.isNotEmpty()) currentDayIndex.coerceIn(days.indices) else 0,
        pageCount = { days.size },
    )
    val coroutineScope = rememberCoroutineScope()

    // Notify the ViewModel when the pager settles on a new page.
    // Uses snapshotFlow + distinctUntilChanged so setCurrentDayIndex is only called
    // when the page actually changes, preventing spurious state updates.
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page -> setCurrentDayIndex(page) }
    }

    // Respond to external navigation (e.g. date picker) by scrolling the pager.
    // The guard ensures this is a no-op when the change originated from the pager
    // itself (in which case currentPage already equals currentDayIndex), breaking
    // any potential cycle between the two sync effects.
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

    // Guard against stale pager state when the days list is updated externally.
    // CalendarPageContent guarantees days is non-empty before CalendarScroller is called,
    // so the fallback to days.last() is only a safety net against edge cases.
    val currentDay = days.getOrElse(pagerState.currentPage) { days.last() }
    val currentDateFormatted = currentDay.date.format(StandardDateTimeFormatter.date)

    // pagerState.pageCount mirrors days.size dynamically via the pageCount lambda, so
    // these callbacks only need pagerState and coroutineScope as stable remember keys.
    val onPrevious: () -> Unit = remember(pagerState, coroutineScope) {
        {
            val target = pagerState.currentPage - 1
            if (target >= 0) {
                coroutineScope.launch { pagerState.animateScrollToPage(target) }
            }
        }
    }
    val onNext: () -> Unit = remember(pagerState, coroutineScope) {
        {
            val target = pagerState.currentPage + 1
            if (target < pagerState.pageCount) {
                coroutineScope.launch { pagerState.animateScrollToPage(target) }
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
                HorizontalPager(
                    state = pagerState,
                    beyondBoundsPageCount = 0,
                    modifier = Modifier.fillMaxSize(),
                ) { pageIndex ->
                    val day = days.getOrNull(pageIndex) ?: return@HorizontalPager
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                    ) {
                        CalendarDayPortrait(
                            day = day,
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
                    val day = days.getOrNull(pageIndex) ?: return@HorizontalPager
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                    ) {
                        CalendarDayLandscape(
                            day = day,
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