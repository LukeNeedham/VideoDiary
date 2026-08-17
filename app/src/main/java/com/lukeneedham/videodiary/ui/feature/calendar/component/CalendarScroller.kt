package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.calendar.component.day.CalendarDayContent
import com.lukeneedham.videodiary.ui.feature.calendar.component.day.bottombar.CalendarDayBottomBar
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerExo
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoToolbarLayout
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.LocalDate

// Fraction of the video's width, on either side, that counts as an edge tap for
// navigating to the previous/next day.
private const val EDGE_TAP_FRACTION = 0.25f

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
    onMenuClick: () -> Unit,
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

    // Update the video controller with the 'settled' video
    // this means the playing video only updates once the scroll completes
    val settledPage = pagerState.settledPage
    LaunchedEffect(settledPage) {
        val settledDay = days[settledPage]
        videoPlayerController.playingVideo = settledDay.video
    }

    // Pause all videos while the pager is being dragged; resume once it settles.
    // Uses snapshotFlow so this only fires on scroll-state transitions, not on
    // every recomposition or at initial composition.
    LaunchedEffect(pagerState, videoPlayerController) {
        snapshotFlow { pagerState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { isScrolling ->
                if (isScrolling) {
                    videoPlayerController.temporaryPause()
                } else {
                    videoPlayerController.temporaryResume()
                }
            }
    }

    // Safe fallback: pagerState may briefly lag behind after days grows, but days is
    // guaranteed non-empty (enforced by the require above), so days.last() is safe.
    val currentDay = days.getOrElse(pagerState.currentPage) { days.last() }
    val currentDateFormatted = currentDay.date.format(StandardDateTimeFormatter.date)

    // pagerState.pageCount mirrors days.size dynamically via the pageCount lambda, so
    // these callbacks only need pagerState and coroutineScope as stable remember keys.
    //
    // Uses targetPage (the page the pager is animating towards, or currentPage if idle)
    // rather than currentPage, which only updates once an in-flight animation settles.
    // Basing the offset on currentPage would make a tap during an ongoing page-flip
    // resolve to the same target already being animated to - i.e. a no-op - making
    // rapid taps feel like they're being ignored until the prior animation finishes.
    val navigateByOffset: (Int) -> Unit = remember(pagerState, coroutineScope) {
        { offset ->
            val target = pagerState.targetPage + offset
            if (target >= 0 && target < pagerState.pageCount) {
                coroutineScope.launch { pagerState.animateScrollToPage(target) }
            }
        }
    }
    val onPrevious: () -> Unit = remember(navigateByOffset) { { navigateByOffset(-1) } }
    val onNext: () -> Unit = remember(navigateByOffset) { { navigateByOffset(1) } }

    VideoToolbarLayout(
        videoAspectRatio = videoAspectRatio,
        topOverlay = {
            CalendarTopBar(
                currentDateFormatted = currentDateFormatted,
                openDayPicker = openDayPicker,
                goToToday = goToToday,
                onMenuClick = onMenuClick,
                isToday = currentDay.isToday,
            )
        },
        bottomBar = {
            CalendarDayBottomBar(
                videoPlayerController = videoPlayerController,
                day = currentDay,
                isEditable = currentDay.isToday || allowEditPastDays,
                onRecordVideoClick = { onRecordVideoClick(currentDay.date) },
                onDeleteVideoClick = { onDeleteVideoClick(currentDay.date) },
                share = share,
                modifier = Modifier.align(Alignment.Center),
            )
        },
    ) { aspectRatio ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(onPrevious, onNext) {
                    awaitEachGesture {
                        while (true) {
                            // PointerEventPass.Initial lets this Box see the touch
                            // BEFORE the horizontal scroll gets a chance to intercept it.
                            val down = awaitFirstDown(pass = PointerEventPass.Initial)
                            videoPlayerController.temporaryPause()

                            val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                            videoPlayerController.temporaryResume()

                            // Only treat this as an edge tap (not a swipe or long press) if
                            // the pointer didn't move beyond touch slop between down and up,
                            // and it was released before the long-press timeout.
                            val pressDurationMillis = up?.let { it.uptimeMillis - down.uptimeMillis }
                            val isTap = up != null &&
                                (up.position - down.position).getDistance() < viewConfiguration.touchSlop &&
                                pressDurationMillis != null &&
                                pressDurationMillis < viewConfiguration.longPressTimeoutMillis
                            if (isTap) {
                                val width = size.width
                                when {
                                    down.position.x < width * EDGE_TAP_FRACTION -> onPrevious()
                                    down.position.x > width * (1f - EDGE_TAP_FRACTION) -> onNext()
                                }
                            }
                        }
                    }
                }
        ) {
            HorizontalPager(
                state = pagerState,
                beyondBoundsPageCount = 0,
                modifier = Modifier.fillMaxSize()
            ) { pageIndex ->
                val day = days.getOrNull(pageIndex) ?: return@HorizontalPager
                val date = day.date
                CalendarDayContent(
                    day = day,
                    videoAspectRatio = aspectRatio,
                    allowEditPastDays = allowEditPastDays,
                    onRecordVideoClick = {
                        onRecordVideoClick(date)
                    },
                    videoPlayerController = videoPlayerController,
                )
            }

            // Hosted once here rather than per-page: recreating VideoPlayerExo's native
            // TextureView on every day navigation left the video area briefly unresponsive
            // to touch. Hidden mid-swipe since it doesn't track page-drag offset itself.
            val playingVideo = videoPlayerController.playingVideo
            if (!LocalInspectionMode.current && playingVideo != null && !pagerState.isScrollInProgress) {
                VideoPlayerExo(
                    video = playingVideo,
                    controller = videoPlayerController,
                )
            }
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
        onMenuClick = {},
        setCurrentDayIndex = {},
        currentDayIndex = 0,
        share = {},
        videoPlayerController = rememberVideoPlayerController(),
    )
}
