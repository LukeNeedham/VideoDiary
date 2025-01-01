package com.lukeneedham.videodiary.ui.feature.record.film

import android.net.Uri
import kotlin.time.Duration

sealed interface RecordingState {
    object Ready : RecordingState
    data class Recording(val duration: Duration) : RecordingState
    data class Failed(val errorCode: Int, val exception: Throwable?) : RecordingState
    data class Success(val output: Uri) : RecordingState
}