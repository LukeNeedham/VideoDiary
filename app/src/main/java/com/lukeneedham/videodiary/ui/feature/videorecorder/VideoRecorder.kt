package com.lukeneedham.videodiary.ui.feature.videorecorder

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import com.lukeneedham.videodiary.domain.util.Timer
import com.lukeneedham.videodiary.domain.util.TimerTick
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors
import kotlin.time.Duration.Companion.seconds

class VideoRecorder(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val videoCapture: VideoCapture<Recorder>,
) {
    private val timer = Timer()
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    /**
     * The duration of each video.
     * Should be user configurable ideally, when first setting up the diary
     */
    private val videoDuration = 3.seconds.inWholeMilliseconds

    private val timestampFormat = "yyyy-MM-dd-HH-mm-ss-SSS"

    fun startRecording(
        onStateUpdate: (RecordingState) -> Unit,
    ) {
        val timestamp = SimpleDateFormat(timestampFormat, Locale.US)
            .format(System.currentTimeMillis())
        val name = "VideoDiary-$timestamp"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/VideoDiary")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions.Builder(
            context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
            .setContentValues(contentValues)
            .build()

        // We already know we have the audio permission
        @SuppressLint("MissingPermission")
        val pendingRecording = videoCapture.output
            .prepareRecording(context, mediaStoreOutputOptions)
            .withAudioEnabled()

        val recording = pendingRecording.start(cameraExecutor) { recordEvent ->
            when (recordEvent) {
                is VideoRecordEvent.Finalize -> {
                    timer.stop()

                    val error = recordEvent.error
                    if (error == VideoRecordEvent.Finalize.ERROR_NONE) {
                        val output = recordEvent.outputResults.outputUri
                        onStateUpdate(RecordingState.Success(output))
                    } else {
                        val ex = recordEvent.cause
                        Log.e("VideoRecorder", "Failed", ex)
                        onStateUpdate(RecordingState.Failed(error, ex))
                    }
                }
            }
        }

        timer.start(
            tickMillis = 100,
            endAtMillis = videoDuration,
            scope = coroutineScope,
        ) { tick ->
            when (tick) {
                is TimerTick.Ongoing -> {
                    val state = RecordingState.Recording(tick.duration)
                    onStateUpdate(state)
                }

                TimerTick.Finished -> {
                    recording.stop()
                }
            }
        }
    }

    fun dispose() {
        cameraExecutor.shutdown()
    }
}