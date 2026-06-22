package com.lukeneedham.videodiary.ui.feature.common.camera

import android.util.Log
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.lukeneedham.videodiary.domain.model.CameraResolutionRotation
import kotlinx.coroutines.delay

@Composable
fun CameraInput(
    currentResolution: Size,
    videoCapture: VideoCapture<Recorder>,
    canZoom: Boolean,
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

    val canZoomUpdated by rememberUpdatedState(canZoom)

    var resolutionMissing by remember(currentResolution) { mutableStateOf(false) }
    var camera: Camera? by remember { mutableStateOf(null) }

    var focusTapOffset by remember { mutableStateOf<Offset?>(null) }
    var focusTapCount by remember { mutableIntStateOf(0) }
    val focusAlpha = remember { Animatable(0f) }

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
                    camera = cameraProvider.bindToLifecycle(
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

    // Animate focus indicator: snap in, hold, then fade out
    LaunchedEffect(focusTapCount) {
        if (focusTapCount == 0) return@LaunchedEffect
        focusAlpha.snapTo(0.7f)
        delay(700)
        focusAlpha.animateTo(0f, animationSpec = tween(500))
        focusTapOffset = null
    }

    val currentFocusTapOffset = focusTapOffset

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val cam = camera ?: return@detectTapGestures
                    val point = previewView.meteringPointFactory.createPoint(offset.x, offset.y)
                    val action = FocusMeteringAction.Builder(point).build()
                    cam.cameraControl.startFocusAndMetering(action)
                    focusTapOffset = offset
                    focusTapCount++
                }
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoomChange, _ ->
                    if (!canZoomUpdated) return@detectTransformGestures
                    val cam = camera ?: return@detectTransformGestures
                    val zoomState = cam.cameraInfo.zoomState.value ?: return@detectTransformGestures
                    val newZoom = zoomState.zoomRatio * zoomChange
                    val boundedNewZoom = newZoom.coerceIn(
                        zoomState.minZoomRatio,
                        zoomState.maxZoomRatio,
                    )
                    camera?.cameraControl?.setZoomRatio(boundedNewZoom)
                }
            }
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

        if (currentFocusTapOffset != null) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = focusAlpha.value),
                    radius = 25.dp.toPx(),
                    center = currentFocusTapOffset,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
    }
}
