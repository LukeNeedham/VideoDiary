package com.lukeneedham.videodiary.ui.feature.setup

import android.util.Size
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.CameraResolutionRotation
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.pageindicator.PageIndicator
import com.lukeneedham.videodiary.ui.feature.permissions.RequestPermissionSlideContent
import com.lukeneedham.videodiary.ui.feature.setup.complete.SetupCompleteSlideContent
import com.lukeneedham.videodiary.ui.feature.setup.duration.SelectVideoDurationPage
import com.lukeneedham.videodiary.ui.feature.setup.intro.SetupIntroSlideContent
import com.lukeneedham.videodiary.ui.feature.setup.intro.setupIntroSlides
import com.lukeneedham.videodiary.ui.feature.setup.orientation.SetupSelectOrientationPage
import com.lukeneedham.videodiary.ui.feature.setup.resolution.SetupSelectResolutionPage
import com.lukeneedham.videodiary.ui.feature.setup.storage.SetupSelectStoragePage
import com.lukeneedham.videodiary.ui.permissions.RequiredPermission
import com.lukeneedham.videodiary.ui.permissions.RequiredPermissions
import com.lukeneedham.videodiary.ui.theme.AppTheme
import kotlinx.coroutines.launch

/**
 * The onboarding flow (intro, permissions, orientation, video storage (only if there is an SD card), resolution, video duration)
 * as one continuous swipeable pager. Pages are only revealed as their prerequisites are met: permission pages reveal one at a
 * time as each is granted, and the setup pages that follow (which rely on the camera) aren't revealed until every permission is granted.
 *
 * The page indicator and the primary action button live outside the pager, so they stay static
 * while only the page content itself swipes. The button is computed directly here from state this
 * composable already holds (permission grants, camera resolution readiness) - the same reliable
 * pattern the page indicator uses - rather than relying on a page composed inside the pager to
 * report it, since pager pages are subcomposed on their own schedule and can't safely drive
 * rendering outside the pager. A page only hands up what its button does when it is pressed
 * (`registerSave`), never how the button looks.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SetupPageContent(
    requiredPermissions: List<RequiredPermission>,
    acquiredPermissions: List<String>,
    hasStoragePage: Boolean,
    resolutions: List<Size>,
    setOrientation: (Orientation) -> Unit,
    requestPermission: (permission: String) -> Unit,
    onSetupComplete: () -> Unit,
) {
    // The storage page is only there if there is an SD card to choose when onboarding starts
    val progress = remember { SetupProgress(hasStoragePage = hasStoragePage) }

    val firstMissingPermissionIndex = requiredPermissions.indexOfFirst { it.permission !in acquiredPermissions }
    val allPermissionsGranted = firstMissingPermissionIndex == -1

    // Resolution's state is hoisted here (rather than owned by its own page) so its readiness can
    // drive the "Next" button, the same reliable way every other page's action is computed.
    var resolutionRotation: CameraResolutionRotation? by remember { mutableStateOf(null) }
    var currentResolutionIndex by remember { mutableIntStateOf(0) }
    val currentResolution = resolutions.getOrNull(currentResolutionIndex)
    var currentResolutionMissing by remember(currentResolution) { mutableStateOf(false) }

    fun setCurrentResolutionIndex(index: Int) {
        val resolutionsCount = resolutions.size
        if (resolutionsCount != 0) {
            currentResolutionIndex = index.mod(resolutionsCount)
        }
    }

    val isResolutionReady = currentResolution != null && !currentResolutionMissing && resolutionRotation != null

    // Pages beyond the current prerequisite aren't revealed yet, so the user can't swipe past a
    // permission without granting it, or past resolution selection without a valid resolution.
    val revealedPageCount = when {
        !allPermissionsGranted -> progress.PERMISSIONS_START_INDEX + firstMissingPermissionIndex + 1
        !isResolutionReady -> progress.RESOLUTION_PAGE_INDEX + 1
        else -> progress.TOTAL_PAGE_COUNT
    }

    val pagerState = rememberPagerState { revealedPageCount }
    val coroutineScope = rememberCoroutineScope()

    fun goToNextPage() {
        coroutineScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    // What the "Next" button of a page saves, by the index of the page. Each page owns its own
    // settings, and hands this up when it is composed; the button only calls it.
    val saveActions = remember { mutableMapOf<Int, () -> Unit>() }

    val currentPage = pagerState.currentPage
    val currentPageAction: PagerAction? = when {
        currentPage < SetupProgress.INTRO_SLIDE_COUNT -> PagerAction(
            label = "Next",
            onClick = ::goToNextPage,
        )

        currentPage < progress.ORIENTATION_PAGE_INDEX -> {
            val permission = requiredPermissions.getOrNull(currentPage - progress.PERMISSIONS_START_INDEX)
            when {
                permission == null -> null
                permission.permission in acquiredPermissions -> PagerAction(
                    label = "Next",
                    onClick = ::goToNextPage,
                )
                else -> PagerAction(
                    label = "Grant permission",
                    onClick = { requestPermission(permission.permission) },
                )
            }
        }

        currentPage == progress.ORIENTATION_PAGE_INDEX -> PagerAction(
            label = "Next",
            onClick = { saveActions[progress.ORIENTATION_PAGE_INDEX]?.invoke() },
        )

        currentPage == progress.STORAGE_PAGE_INDEX -> PagerAction(
            label = "Next",
            onClick = { saveActions[progress.STORAGE_PAGE_INDEX]?.invoke() },
        )

        currentPage == progress.RESOLUTION_PAGE_INDEX -> PagerAction(
            label = "Next",
            enabled = isResolutionReady,
            onClick = { saveActions[progress.RESOLUTION_PAGE_INDEX]?.invoke() },
        )

        currentPage == progress.DURATION_PAGE_INDEX -> PagerAction(
            label = "Next",
            onClick = { saveActions[progress.DURATION_PAGE_INDEX]?.invoke() },
        )

        currentPage == progress.FINAL_PAGE_INDEX -> PagerAction(
            label = "Get started",
            onClick = onSetupComplete,
        )

        else -> null
    }

    Column(modifier = Modifier.fillMaxSize().navigationBarsPadding()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            when {
                page < SetupProgress.INTRO_SLIDE_COUNT -> SetupIntroSlideContent(
                    slide = setupIntroSlides[page],
                )

                page < progress.ORIENTATION_PAGE_INDEX -> {
                    val permission = requiredPermissions[page - progress.PERMISSIONS_START_INDEX]
                    RequestPermissionSlideContent(
                        permission = permission,
                        isGranted = permission.permission in acquiredPermissions,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                page == progress.ORIENTATION_PAGE_INDEX -> SetupSelectOrientationPage(
                    onContinue = ::goToNextPage,
                    setOrientation = setOrientation,
                    registerSave = { saveActions[progress.ORIENTATION_PAGE_INDEX] = it },
                )

                page == progress.STORAGE_PAGE_INDEX -> SetupSelectStoragePage(
                    onContinue = ::goToNextPage,
                    registerSave = { saveActions[progress.STORAGE_PAGE_INDEX] = it },
                )

                page == progress.RESOLUTION_PAGE_INDEX -> SetupSelectResolutionPage(
                    onContinue = ::goToNextPage,
                    registerSave = { saveActions[progress.RESOLUTION_PAGE_INDEX] = it },
                    resolutions = resolutions,
                    currentResolutionIndex = currentResolutionIndex,
                    setCurrentResolutionIndex = ::setCurrentResolutionIndex,
                    rotation = resolutionRotation,
                    onResolutionLoaded = { _, isMissing, loadedRotation ->
                        currentResolutionMissing = isMissing
                        resolutionRotation = loadedRotation
                    },
                )

                page == progress.DURATION_PAGE_INDEX -> SelectVideoDurationPage(
                    onContinue = ::goToNextPage,
                    registerSave = { saveActions[progress.DURATION_PAGE_INDEX] = it },
                )

                else -> SetupCompleteSlideContent()
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        ) {
            PageIndicator(
                pageCount = progress.TOTAL_PAGE_COUNT,
                currentPageIndex = currentPage,
                color = Color.Black,
            )
        }

        // Reserve the button's height even when there's no action to show, so the page doesn't
        // jump right before an automatic transition to the next page.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp)
                .padding(15.dp),
        ) {
            if (currentPageAction != null) {
                Button(
                    text = currentPageAction.label,
                    enabled = currentPageAction.enabled,
                    onClick = currentPageAction.onClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        SetupPageContent(
            requiredPermissions = RequiredPermissions.permissions,
            acquiredPermissions = emptyList(),
            hasStoragePage = true,
            resolutions = emptyList(),
            setOrientation = {},
            requestPermission = {},
            onSetupComplete = {},
        )
    }
}
