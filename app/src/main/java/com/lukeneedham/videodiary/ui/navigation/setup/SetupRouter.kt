package com.lukeneedham.videodiary.ui.navigation.setup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.pageindicator.PageIndicator
import com.lukeneedham.videodiary.ui.feature.permissions.RequestPermissionPage
import com.lukeneedham.videodiary.ui.feature.permissions.RequestPermissionsViewModel
import com.lukeneedham.videodiary.ui.feature.setup.duration.SelectVideoDurationPage
import com.lukeneedham.videodiary.ui.feature.setup.duration.SelectVideoDurationViewModel
import com.lukeneedham.videodiary.ui.feature.setup.intro.SetupIntroSlidePage
import com.lukeneedham.videodiary.ui.feature.setup.intro.setupIntroSlides
import com.lukeneedham.videodiary.ui.feature.setup.orientation.SetupSelectOrientationPage
import com.lukeneedham.videodiary.ui.feature.setup.orientation.SetupSelectOrientationViewModel
import com.lukeneedham.videodiary.ui.feature.setup.resolution.SetupSelectResolutionPage
import com.lukeneedham.videodiary.ui.permissions.PermissionResultListenerHolder
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/**
 * The whole onboarding flow (intro, permissions, orientation, resolution, video duration) is one
 * continuous swipeable pager. Pages are only revealed as their prerequisites are met: permission
 * pages reveal one at a time as each is granted, and the setup pages that follow (which rely on
 * the camera) aren't revealed until every permission is granted.
 *
 * The page indicator and the primary action button live outside the pager, so they stay static
 * while only the page content itself swipes. The button is computed directly here from state this
 * composable already holds (view models, permission grants) - the same reliable pattern the page
 * indicator uses - rather than relying on a page composed inside the pager to report it, since
 * pager pages are subcomposed on their own schedule and can't safely drive rendering outside the
 * pager. Resolution is the one exception: its "Next" button depends on local, asynchronously
 * loaded camera readiness, so it keeps its own internal button instead.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetupRouter(
    onSetupComplete: () -> Unit,
    setOrientation: (Orientation) -> Unit,
    requestPermission: (permission: String) -> Unit,
    permissionResultListenerHolder: PermissionResultListenerHolder,
    onPermissionsAcquired: () -> Unit,
) {
    val permissionsViewModel: RequestPermissionsViewModel = koinViewModel()
    val orientationViewModel: SetupSelectOrientationViewModel = koinViewModel()
    val durationViewModel: SelectVideoDurationViewModel = koinViewModel()

    LaunchedEffect(permissionResultListenerHolder, permissionsViewModel) {
        permissionResultListenerHolder.onPermissionResult = permissionsViewModel::onPermissionResult
    }

    val requiredPermissions = permissionsViewModel.requiredPermissions
    val acquiredPermissions = permissionsViewModel.acquiredPermissions
    val firstMissingPermissionIndex = requiredPermissions.indexOfFirst { it.permission !in acquiredPermissions }
    val allPermissionsGranted = firstMissingPermissionIndex == -1

    LaunchedEffect(allPermissionsGranted) {
        if (allPermissionsGranted) onPermissionsAcquired()
    }

    val revealedPageCount = if (allPermissionsGranted) {
        SetupProgress.TOTAL_PAGE_COUNT
    } else {
        SetupProgress.PERMISSIONS_START_INDEX + firstMissingPermissionIndex + 1
    }

    val pagerState = rememberPagerState { revealedPageCount }
    val coroutineScope = rememberCoroutineScope()

    fun goToNextPage() {
        coroutineScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    val currentPage = pagerState.currentPage
    val currentPageAction: PagerAction? = when {
        currentPage < SetupProgress.INTRO_SLIDE_COUNT -> PagerAction(
            label = "Next",
            onClick = ::goToNextPage,
        )

        currentPage < SetupProgress.ORIENTATION_PAGE_INDEX -> {
            val permission = requiredPermissions.getOrNull(currentPage - SetupProgress.PERMISSIONS_START_INDEX)
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

        currentPage == SetupProgress.ORIENTATION_PAGE_INDEX -> PagerAction(
            label = "Next",
            onClick = { orientationViewModel.saveSettings() },
        )

        currentPage == SetupProgress.RESOLUTION_PAGE_INDEX -> null

        currentPage == SetupProgress.DURATION_PAGE_INDEX -> PagerAction(
            label = "Next",
            onClick = { durationViewModel.saveSettings() },
        )

        else -> null
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            when {
                page < SetupProgress.INTRO_SLIDE_COUNT -> SetupIntroSlidePage(
                    slide = setupIntroSlides[page],
                )

                page < SetupProgress.ORIENTATION_PAGE_INDEX -> {
                    val permission = requiredPermissions[page - SetupProgress.PERMISSIONS_START_INDEX]
                    RequestPermissionPage(
                        permission = permission,
                        isGranted = permission.permission in acquiredPermissions,
                    )
                }

                page == SetupProgress.ORIENTATION_PAGE_INDEX -> SetupSelectOrientationPage(
                    viewModel = orientationViewModel,
                    onContinue = ::goToNextPage,
                    setOrientation = setOrientation,
                )

                page == SetupProgress.RESOLUTION_PAGE_INDEX -> SetupSelectResolutionPage(
                    viewModel = koinViewModel(),
                    onContinue = ::goToNextPage,
                )

                else -> SelectVideoDurationPage(
                    viewModel = durationViewModel,
                    onContinue = onSetupComplete,
                )
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        ) {
            PageIndicator(
                pageCount = SetupProgress.TOTAL_PAGE_COUNT,
                currentPageIndex = currentPage,
                color = Color.Black,
            )
        }

        // Resolution renders its own internal button (see the class doc), so no space is reserved
        // for it here - only its content and the page indicator above take up the pager's height.
        if (currentPage != SetupProgress.RESOLUTION_PAGE_INDEX) {
            // Reserve the button's height even when there's no action to show, so the page
            // doesn't jump right before an automatic transition to the next page.
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
}
