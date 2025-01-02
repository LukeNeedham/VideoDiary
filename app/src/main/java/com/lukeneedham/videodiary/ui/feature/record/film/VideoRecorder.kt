package com.lukeneedham.videodiary.ui.feature.record.film

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import com.lukeneedham.videodiary.domain.util.Timer
import com.lukeneedham.videodiary.domain.util.TimerTick
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors

class VideoRecorder(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val videoCapture: VideoCapture<Recorder>,
) {
    private val timer = Timer()
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    private val timestampFormat = "yyyy-MM-dd-HH-mm-ss-SSS"

    private var recordingInProgress: Recording? = null

    fun startRecording(
        videoDurationMillis: Long,
        onStateUpdate: (RecordingState) -> Unit,
    ) {
        disposeRunningRecording()

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

        recordingInProgress = recording

        timer.start(
            tickMillis = 100,
            endAtMillis = videoDurationMillis,
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
        disposeRunningRecording()
        cameraExecutor.shutdown()
    }

    private fun disposeRunningRecording() {
        timer.stop()
        recordingInProgress?.stop()
        recordingInProgress = null
    }
}