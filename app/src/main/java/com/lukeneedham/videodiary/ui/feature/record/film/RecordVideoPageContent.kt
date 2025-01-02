package com.lukeneedham.videodiary.ui.feature.record.film

import android.net.Uri
import android.util.Size
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.util.logger.Logger
import com.lukeneedham.videodiary.ui.feature.common.camera.CameraInput
import com.lukeneedham.videodiary.ui.feature.record.common.RecordVideoActionBarSize
import com.lukeneedham.videodiary.ui.feature.record.film.component.RecordVideoActionBar

@Composable
fun RecordVideoPageContent(
    videoDurationMillis: Long,
    resolution: Size,
    quality: Quality,
    onRecordingFinished: (videoContentUri: Uri) -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var state: RecordingState by remember { mutableStateOf(RecordingState.Ready) }

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
        CameraInput(
            currentResolution = resolution,
            videoCapture = videoCapture,
            onResolutionLoaded = { resolution, isMissing, loadedRotation ->
                if (isMissing) {
                    Logger.error("No camera feed available for resolution: $resolution")
                }
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        RecordVideoActionBar(
            state = currentState,
            onRecordClick = {
                record()
            },
            onCloseClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
internal fun PreviewRecordVideoPage() {
    RecordVideoPageContent(
        quality = Quality.HD,
        resolution = Size(100, 300),
        onRecordingFinished = {},
        videoDurationMillis = 3000,
        onBack = {},
    )
}