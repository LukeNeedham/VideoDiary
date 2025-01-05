package com.lukeneedham.videodiary.ui.feature.setup.resolution

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraMetadata
import android.util.Size
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.lukeneedham.videodiary.domain.model.CameraResolutionRotation
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.camera.CameraInput
import com.lukeneedham.videodiary.ui.feature.common.camera.CameraQualityEffect
import com.lukeneedham.videodiary.ui.feature.common.pageindicator.PageIndicator
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar

@OptIn(ExperimentalCamera2Interop::class)
@Composable
fun SetupPageContent(
    onContinueClick: (resolution: Size, rotation: CameraResolutionRotation) -> Unit,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    val context = LocalContext.current

    var resolutions by remember { mutableStateOf(emptyList<Size>()) }
    var rotation: CameraResolutionRotation? by remember { mutableStateOf(null) }
    var currentResolutionIndex by remember { mutableIntStateOf(0) }
    val currentResolution = resolutions.getOrNull(currentResolutionIndex)
    var currentQuality: Quality? by remember { mutableStateOf(null) }

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

    val currentResolutionName = run {
        if (currentResolution == null) return@run null
        val rotationLocal = rotation ?: return@run null
        val rotatedResolution = rotationLocal.rotate(currentResolution)
        "${rotatedResolution.width}x${rotatedResolution.height}"
    }

    fun setCurrentResolutionIndex(index: Int) {
        val resolutionsCount = resolutions.size
        if (resolutionsCount != 0) {
            currentResolutionIndex = index.mod(resolutionsCount)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GenericToolbar(
            canGoBack = canGoBack, onBack = onBack,
        )

        Spacer(modifier = Modifier.height(2.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            ResolutionSelector(
                currentResolutionIndex = currentResolutionIndex,
                setCurrentResolutionIndex = ::setCurrentResolutionIndex,
                currentResolutionName = currentResolutionName,
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                PageIndicator(
                    pageCount = resolutions.size,
                    currentPageIndex = currentResolutionIndex,
                    color = Color.Black,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            CameraQualityEffect(currentResolution) {
                currentQuality = it
            }

            val currentQualityLocal = currentQuality
            if (currentQualityLocal != null && currentResolution != null) {
                // Dummy recorder and video capture -
                // just used to configure the preview with the selected quality
                val recorder = remember(currentQualityLocal) {
                    Recorder.Builder()
                        .setQualitySelector(
                            QualitySelector.from(currentQualityLocal)
                        )
                        .build()
                }
                val videoCapture = remember(recorder) {
                    VideoCapture.Builder(recorder).build()
                }

                CameraInput(
                    videoCapture = videoCapture,
                    currentResolution = currentResolution,
                    onResolutionLoaded = { resolution, isMissing, loadedRotation ->
                        currentResolutionMissing = isMissing
                        rotation = loadedRotation
                    },
                    canZoom = false,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }

        val rotationLocal = rotation
        Button(
            text = "Next",
            enabled = currentResolution != null && !currentResolutionMissing && rotationLocal != null,
            onClick = {
                if (currentResolution != null && rotationLocal != null) {
                    onContinueClick(currentResolution, rotationLocal)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        )
    }
}

@Preview
@Composable
internal fun PreviewSetupPageContent() {
    SetupPageContent(
        onContinueClick = { _, _ -> }, canGoBack = true, onBack = {},
    )
}