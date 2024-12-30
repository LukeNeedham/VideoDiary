package com.lukeneedham.videodiary.ui.feature.videorecorder

import android.net.Uri
import android.util.Size
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.lukeneedham.videodiary.domain.util.logger.Logger
import com.lukeneedham.videodiary.ui.feature.videorecorder.component.CameraInput

@Composable
fun RecordVideoPageContent(
    resolution: Size,
    quality: Quality,
    onRecordingFinished: (videoContentUri: Uri) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var state: RecordingState by remember { mutableStateOf(RecordingState.Ready) }
    val currentState = state

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
        videoRecorder.startRecording {
            state = it
        }
    }

    Box(
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
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            @Composable
            fun Message(
                text: String,
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .background(color = Color.White.copy(0.5f), shape = CircleShape)
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                ) {
                    Text(
                        text = text,
                        color = Color.Black,
                    )
                }
            }

            when (currentState) {
                is RecordingState.Failed ->
                    Message(text = "Error! ${currentState.errorCode} : ${currentState.exception}")

                RecordingState.Ready -> {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape,
                            )
                            .padding(3.dp)
                            .background(
                                color = Color.Red,
                                shape = CircleShape,
                            )
                            .clickable {
                                record()
                            }
                    )
                }

                is RecordingState.Recording ->
                    Message(text = currentState.duration.toString())

                is RecordingState.Success ->
                    Message(text = "Done")
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
        onRecordingFinished = {},
    )
}