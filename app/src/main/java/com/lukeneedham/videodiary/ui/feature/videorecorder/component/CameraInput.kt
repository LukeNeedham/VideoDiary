package com.lukeneedham.videodiary.ui.feature.videorecorder.component

import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.lukeneedham.videodiary.domain.model.CameraResolutionRotation

@Composable
fun CameraInput(
    currentResolution: Size,
    videoCapture: VideoCapture<Recorder>,
    onResolutionLoaded: (resolution: Size, isMissing: Boolean, rotation: CameraResolutionRotation) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FIT_CENTER
        }
    }

    var resolutionMissing by remember(currentResolution) { mutableStateOf(false) }

    val previewUseCase = remember(previewView, currentResolution) {
        val resolutionSelector = ResolutionSelector.Builder()
            .setResolutionFilter { supportedSizes, rotationDegrees ->
                val rotation = CameraResolutionRotation.fromDegrees(rotationDegrees)
                    ?: CameraResolutionRotation.R0
                val sizes = supportedSizes.filter { it == currentResolution }
                val isMissing = sizes.isEmpty()
                onResolutionLoaded(currentResolution, isMissing, rotation)
                if (isMissing) {
                    resolutionMissing = true
                }
                sizes
            }.build()

        Preview.Builder()
            .setResolutionSelector(resolutionSelector)
            .build()
            .apply {
                surfaceProvider = previewView.surfaceProvider
            }
    }

    fun setupPreview() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        previewUseCase,
                        videoCapture,
                    )
                } catch (ex: Exception) {
                    Log.e("CameraX", "Use case binding failed", ex)
                }
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    LaunchedEffect(previewView, videoCapture) {
        setupPreview()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        if (resolutionMissing) {
            Text(
                text = "Resolution is not available!",
                color = Color.White,
            )
        } else {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
