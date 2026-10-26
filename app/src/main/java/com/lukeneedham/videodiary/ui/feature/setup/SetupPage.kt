package com.lukeneedham.videodiary.ui.feature.setup

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraMetadata
import android.util.Size
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.QualitySelector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.lukeneedham.videodiary.domain.model.Orientation
import com.lukeneedham.videodiary.ui.permissions.PermissionResultListenerHolder
import org.koin.compose.viewmodel.koinViewModel

/**
 * The whole onboarding flow (intro, permissions, orientation, resolution, video duration) as one
 * page: a continuous swipeable pager, drawn by [SetupPageContent]. This connects it to the
 * permission state and loads the camera resolutions the resolution step offers.
 */
@androidx.annotation.OptIn(ExperimentalCamera2Interop::class)
@Composable
fun SetupPage(
    onSetupComplete: () -> Unit,
    setOrientation: (Orientation) -> Unit,
    requestPermission: (permission: String) -> Unit,
    permissionResultListenerHolder: PermissionResultListenerHolder,
    onPermissionsAcquired: () -> Unit,
) {
    val viewModel = koinViewModel<SetupViewModel>()

    LaunchedEffect(permissionResultListenerHolder, viewModel) {
        permissionResultListenerHolder.onPermissionResult = viewModel::onPermissionResult
    }

    val requiredPermissions = viewModel.requiredPermissions
    val acquiredPermissions = viewModel.acquiredPermissions
    val allPermissionsGranted = requiredPermissions.all { it.permission in acquiredPermissions }

    LaunchedEffect(allPermissionsGranted) {
        if (allPermissionsGranted) onPermissionsAcquired()
    }

    val context = LocalContext.current
    var resolutions by remember { mutableStateOf(emptyList<Size>()) }

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

    SetupPageContent(
        requiredPermissions = requiredPermissions,
        acquiredPermissions = acquiredPermissions,
        resolutions = resolutions,
        setOrientation = setOrientation,
        requestPermission = requestPermission,
        onSetupComplete = onSetupComplete,
    )
}
