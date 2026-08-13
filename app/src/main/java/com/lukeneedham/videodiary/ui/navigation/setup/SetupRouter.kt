package com.lukeneedham.videodiary.ui.navigation.setup

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraMetadata
import android.util.Size
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.QualitySelector
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.lukeneedham.videodiary.domain.model.CameraResolutionRotation
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
import com.lukeneedham.videodiary.ui.feature.setup.resolution.SetupSelectResolutionViewModel
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
 * composable already holds (view models, permission grants, camera resolution readiness) - the
 * same reliable pattern the page indicator uses - rather than relying on a page composed inside
 * the pager to report it, since pager pages are subcomposed on their own schedule and can't safely
 * drive rendering outside the pager.
 */
@OptIn(ExperimentalFoundationApi::class)
@androidx.annotation.OptIn(ExperimentalCamera2Interop::class)
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
    val resolutionViewModel: SetupSelectResolutionViewModel = koinViewModel()
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

    // Resolution's camera state is hoisted here (rather than owned by its own page content) so
    // its readiness can drive the router's own "Next" button, the same reliable way every other
    // page's action is computed.
    val context = LocalContext.current
    var resolutions by remember { mutableStateOf(emptyList<Size>()) }
    var resolutionRotation: CameraResolutionRotation? by remember { mutableStateOf(null) }
    var currentResolutionIndex by remember { mutableIntStateOf(0) }
    val currentResolution = resolutions.getOrNull(currentResolutionIndex)
    var currentResolutionMissing by remember(currentResolution) { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val cameraInfos = cameraProvider.availableCameraInfos.filter {
                    Camera2CameraInfo
                        .from(it)
                        .getCameraCharacteristic(CameraCharacteristics.LENS_FACING) == CameraMetadata.LENS_FACING_BACK
                }

                val cameraInfo = cameraInfos.first()
                resolutions =
                    QualitySelector.getSupportedQualities(cameraInfo).mapNotNull { quality ->
                        QualitySelector.getResolution(cameraInfo, quality)
                    }
                        // Show the resolution with the most pixels first
                        .sortedByDescending {
                            it.width * it.height
                        }
            },
            ContextCompat.getMainExecutor(context)
        )
    }

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
        !allPermissionsGranted -> SetupProgress.PERMISSIONS_START_INDEX + firstMissingPermissionIndex + 1
        !isResolutionReady -> SetupProgress.RESOLUTION_PAGE_INDEX + 1
        else -> SetupProgress.TOTAL_PAGE_COUNT
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

        currentPage == SetupProgress.RESOLUTION_PAGE_INDEX -> {
            val rotationLocal = resolutionRotation
            PagerAction(
                label = "Next",
                onClick = {
                    if (currentResolution != null && rotationLocal != null) {
                        resolutionViewModel.saveSettings(currentResolution, rotationLocal)
                    }
                },
            )
        }

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
                    viewModel = resolutionViewModel,
                    onContinue = ::goToNextPage,
                    resolutions = resolutions,
                    currentResolutionIndex = currentResolutionIndex,
                    setCurrentResolutionIndex = ::setCurrentResolutionIndex,
                    rotation = resolutionRotation,
                    onResolutionLoaded = { _, isMissing, loadedRotation ->
                        currentResolutionMissing = isMissing
                        resolutionRotation = loadedRotation
                    },
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
