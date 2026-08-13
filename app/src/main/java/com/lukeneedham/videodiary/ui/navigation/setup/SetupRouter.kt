package com.lukeneedham.videodiary.ui.navigation.setup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.ui.feature.permissions.RequestPermissionPage
import com.lukeneedham.videodiary.ui.feature.permissions.RequestPermissionsViewModel
import com.lukeneedham.videodiary.ui.feature.setup.duration.SelectVideoDurationPage
import com.lukeneedham.videodiary.ui.feature.setup.intro.SetupIntroSlidePage
import com.lukeneedham.videodiary.ui.feature.setup.intro.setupIntroSlides
import com.lukeneedham.videodiary.ui.feature.setup.orientation.SetupSelectOrientationPage
import com.lukeneedham.videodiary.ui.feature.setup.resolution.SetupSelectResolutionPage
import com.lukeneedham.videodiary.ui.permissions.PermissionResultListenerHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private const val AUTO_CONTINUE_DELAY_MILLIS = 500L

/**
 * The whole onboarding flow (intro, permissions, orientation, resolution, video duration) is one
 * continuous swipeable pager. Pages are only revealed as their prerequisites are met: permission
 * pages reveal one at a time as each is granted, and the setup pages that follow (which rely on
 * the camera) aren't revealed until every permission is granted.
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

    // Once the permission shown on the settled page is granted, automatically move on to the next
    // page. Keyed on settledPage (not currentPage) so this isn't cancelled and restarted by its
    // own in-flight scroll animation, or by the user's drag, mid-transition.
    val settledPage = pagerState.settledPage
    val settledPermission = requiredPermissions.getOrNull(settledPage - SetupProgress.PERMISSIONS_START_INDEX)
    val isSettledPermissionGranted = settledPermission != null && settledPermission.permission in acquiredPermissions

    LaunchedEffect(settledPage, isSettledPermissionGranted) {
        if (!isSettledPermissionGranted) return@LaunchedEffect
        delay(AUTO_CONTINUE_DELAY_MILLIS)
        pagerState.animateScrollToPage(settledPage + 1)
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
    ) { page ->
        when {
            page < SetupProgress.INTRO_SLIDE_COUNT -> SetupIntroSlidePage(
                slide = setupIntroSlides[page],
                pageIndex = page,
                onNext = ::goToNextPage,
            )

            page < SetupProgress.ORIENTATION_PAGE_INDEX -> {
                val permission = requiredPermissions[page - SetupProgress.PERMISSIONS_START_INDEX]
                RequestPermissionPage(
                    permission = permission,
                    isGranted = permission.permission in acquiredPermissions,
                    requestPermission = requestPermission,
                    pageIndex = page,
                    totalPageCount = SetupProgress.TOTAL_PAGE_COUNT,
                )
            }

            page == SetupProgress.ORIENTATION_PAGE_INDEX -> SetupSelectOrientationPage(
                viewModel = koinViewModel(),
                onContinue = ::goToNextPage,
                setOrientation = setOrientation,
            )

            page == SetupProgress.RESOLUTION_PAGE_INDEX -> SetupSelectResolutionPage(
                viewModel = koinViewModel(),
                onContinue = ::goToNextPage,
            )

            else -> SelectVideoDurationPage(
                viewModel = koinViewModel(),
                onContinue = onSetupComplete,
            )
        }
    }
}
