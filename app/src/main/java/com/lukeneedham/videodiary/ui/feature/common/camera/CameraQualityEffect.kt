package com.lukeneedham.videodiary.ui.feature.common.camera

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraMetadata
import android.util.Size
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@OptIn(ExperimentalCamera2Interop::class)
@Composable
fun CameraQualityEffect(
    resolution: Size?,
    onQuality: (Quality?) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(resolution) {
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
                val qualityAndSizes =
                    QualitySelector.getSupportedQualities(cameraInfo).map { quality ->
                        val res = QualitySelector.getResolution(cameraInfo, quality)
                        quality to res
                    }

                val quality = qualityAndSizes.firstOrNull { it.second == resolution }?.first
                onQuality(quality)
            },
            ContextCompat.getMainExecutor(context)
        )
    }
}