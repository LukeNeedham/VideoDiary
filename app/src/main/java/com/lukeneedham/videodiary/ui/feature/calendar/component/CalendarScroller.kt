package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.calendar.component.day.CalendarDayContent
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarScroller(
    days: List<Day>,
    currentDayIndex: Int,
    videoAspectRatio: Float,
    allowEditPastDays: Boolean,
    onRecordVideoClick: (date: LocalDate) -> Unit,
    onDeleteVideoClick: (date: LocalDate) -> Unit,
    openDayPicker: () -> Unit,
    goToToday: () -> Unit,
    setCurrentDayIndex: (Int) -> Unit,
    share: (ShareRequest) -> Unit,
    videoPlayerController: VideoPlayerController,
) {
    // CalendarPageContent ensures days is always non-empty before reaching this composable.
    require(days.isNotEmpty()) { "CalendarScroller requires a non-empty days list" }

    val pagerState = rememberPagerState(
        initialPage = currentDayIndex.coerceIn(days.indices),
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
    // Uses snapshotFlow so this only fires on scroll-state transitions, not on
    // every recomposition or at initial composition.
    LaunchedEffect(pagerState, videoPlayerController) {
        snapshotFlow { pagerState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { isScrolling ->
                if (isScrolling) videoPlayerController.temporaryPause()
                else videoPlayerController.temporaryResume()
            }
    }

    // Safe fallback: pagerState may briefly lag behind after days grows, but days is
    // guaranteed non-empty (enforced by the require above), so days.last() is safe.
    val currentDay = days.getOrElse(pagerState.currentPage) { days.last() }
    val currentDateFormatted = currentDay.date.format(StandardDateTimeFormatter.date)

    // pagerState.pageCount mirrors days.size dynamically via the pageCount lambda, so
    // these callbacks only need pagerState and coroutineScope as stable remember keys.
    val navigateByOffset: (Int) -> Unit = remember(pagerState, coroutineScope) {
        { offset ->
            val target = pagerState.currentPage + offset
            if (target >= 0 && target < pagerState.pageCount) {
                coroutineScope.launch { pagerState.animateScrollToPage(target) }
            }
        }
    }
    val onPrevious: () -> Unit = remember(navigateByOffset) { { navigateByOffset(-1) } }
    val onNext: () -> Unit = remember(navigateByOffset) { { navigateByOffset(1) } }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            CalendarTopBar(
                currentDateFormatted = currentDateFormatted,
                onPrevious = onPrevious,
                onNext = onNext,
                openDayPicker = openDayPicker,
                goToToday = goToToday,
                isToday = currentDay.isToday,
            )
        }

        HorizontalPager(
            state = pagerState,
            beyondBoundsPageCount = 0,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(videoAspectRatio),
        ) { pageIndex ->
            val day = days.getOrNull(pageIndex) ?: return@HorizontalPager
            val date = day.date
            CalendarDayContent(
                day = day,
                videoAspectRatio = videoAspectRatio,
                allowEditPastDays = allowEditPastDays,
                onRecordVideoClick = {
                    onRecordVideoClick(date)
                },
                onDeleteVideoClick = {
                    onDeleteVideoClick(date)
                },
                videoPlayerController = videoPlayerController,
                share = share,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
internal fun PreviewCalendarScroller() {
    CalendarScroller(
        days = MockDataCalendar.days,
        videoAspectRatio = 1f,
        allowEditPastDays = false,
        onRecordVideoClick = {},
        onDeleteVideoClick = {},
        openDayPicker = {},
        goToToday = {},
        setCurrentDayIndex = {},
        currentDayIndex = 0,
        share = {},
        videoPlayerController = rememberVideoPlayerController(),
    )
}
