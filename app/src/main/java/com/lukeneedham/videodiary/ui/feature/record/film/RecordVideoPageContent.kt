package com.lukeneedham.videodiary.ui.feature.record.film

import android.net.Uri
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.util.logger.Logger
import com.lukeneedham.videodiary.ui.feature.common.camera.CameraInput
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassRecordButton
import com.lukeneedham.videodiary.ui.feature.common.glass.TopScrim
import com.lukeneedham.videodiary.ui.feature.record.film.component.CameraControlSlider
import com.lukeneedham.videodiary.ui.feature.record.film.component.RecordingCountdownButton

@Composable
fun RecordVideoPageContent(
    videoDurationMillis: Long,
    resolution: Size,
    quality: Quality,
    videoAspectRatio: Float,
    onRecordingFinished: (videoContentUri: Uri) -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var state: RecordingState by remember { mutableStateOf(RecordingState.Ready) }
    var camera: Camera? by remember { mutableStateOf(null) }
    var brightnessValue by remember { mutableFloatStateOf(Float.NaN) }
    var zoomValue by remember { mutableFloatStateOf(Float.NaN) }

    LaunchedEffect(camera) {
        val cam = camera ?: return@LaunchedEffect
        val exposureState = cam.cameraInfo.exposureState
        val range = exposureState.exposureCompensationRange
        if (range.lower < range.upper) {
            val current = exposureState.exposureCompensationIndex
            brightnessValue = ((current - range.lower).toFloat() / (range.upper - range.lower))
        }
        val zoomState = cam.cameraInfo.zoomState.value
        if (zoomState != null) {
            zoomValue = zoomState.linearZoom
        }
    }

    val recorder = remember(quality) {
        Recorder.Builder()
            .setQualitySelector(
                QualitySelector.from(quality)
            )
            .build()
    }

    val videoCapture = remember(recorder) {
        VideoCapture.Builder(recorder).build()
    }
    val videoRecorder = remember(videoCapture) {
        VideoRecorder(
            context,
            coroutineScope,
            videoCapture,
        )
    }

    val currentState = state
    LaunchedEffect(currentState) {
        when (currentState) {
            is RecordingState.Failed,
            RecordingState.Ready,
            is RecordingState.Recording -> {
                // No side-effects
            }

            is RecordingState.Success -> {
                onRecordingFinished(currentState.output)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            videoRecorder.dispose()
        }
    }

    fun record() {
        videoRecorder.startRecording(
            videoDurationMillis = videoDurationMillis,
        ) {
            state = it
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(videoAspectRatio)
        ) {
            CameraInput(
                currentResolution = resolution,
                videoCapture = videoCapture,
                onResolutionLoaded = { resolution, isMissing, loadedRotation ->
                    if (isMissing) {
                        Logger.error("No camera feed available for resolution: $resolution")
                    }
                },
                canZoom = true,
                onCameraReady = { camera = it },
                modifier = Modifier.fillMaxSize()
            )

            TopScrim(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .height(140.dp)
            )

            GlassIconButton(
                iconRes = R.drawable.close,
                contentDescription = "Close",
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .safeDrawingPadding()
                    .padding(16.dp)
            )

            val cam = camera
            if (cam != null) {
                val exposureState = cam.cameraInfo.exposureState
                val exposureRange = exposureState.exposureCompensationRange
                val supportsExposure = exposureRange.lower < exposureRange.upper

                if (supportsExposure && !brightnessValue.isNaN()) {
                    CameraControlSlider(
                        value = brightnessValue,
                        onValueChange = { newValue ->
                            brightnessValue = newValue
                            val index = exposureRange.lower +
                                    ((exposureRange.upper - exposureRange.lower) * newValue).toInt()
                            cam.cameraControl.setExposureCompensationIndex(index)
                        },
                        iconRes = R.drawable.brightness,
                        contentDescription = "Brightness",
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .fillMaxHeight(0.6f)
                            .padding(start = 8.dp),
                    )
                }

                if (!zoomValue.isNaN()) {
                    CameraControlSlider(
                        value = zoomValue,
                        onValueChange = { newValue ->
                            zoomValue = newValue
                            cam.cameraControl.setLinearZoom(newValue)
                        },
                        iconRes = R.drawable.zoom,
                        contentDescription = "Zoom",
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight(0.6f)
                            .padding(end = 8.dp),
                    )
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
                .navigationBarsPadding()
        ) {
            val centerButtonModifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxHeight()
                .aspectRatio(1f)
            if (currentState is RecordingState.Recording) {
                val remainingSeconds = ((videoDurationMillis - currentState.duration.inWholeMilliseconds) / 1000.0)
                    .coerceAtLeast(0.0)
                val remainingText = "%.1f".format(remainingSeconds)
                RecordingCountdownButton(
                    remainingText = remainingText,
                    onClick = {
                        videoRecorder.cancelRecording()
                        state = RecordingState.Ready
                    },
                    modifier = centerButtonModifier,
                )
            } else {
                GlassRecordButton(
                    isRecording = false,
                    enabled = currentState == RecordingState.Ready,
                    onClick = { record() },
                    modifier = centerButtonModifier,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewRecordVideoPage() {
    RecordVideoPageContent(
        quality = Quality.HD,
        resolution = Size(100, 300),
        videoAspectRatio = 100f / 300f,
        onRecordingFinished = {},
        videoDurationMillis = 3000,
        onBack = {},
    )
}
