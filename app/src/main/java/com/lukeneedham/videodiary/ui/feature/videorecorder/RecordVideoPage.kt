package com.lukeneedham.videodiary.ui.feature.videorecorder

import android.net.Uri
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.video.Quality
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lukeneedham.videodiary.ui.feature.videorecorder.component.CameraQualityEffect

@OptIn(ExperimentalCamera2Interop::class)
@Composable
fun RecordVideoPage(
    viewModel: RecordVideoViewModel,
    onRecordingFinished: (videoContentUri: Uri) -> Unit,
) {
    var quality: Quality? by remember { mutableStateOf(null) }

    val resolution = viewModel.resolution

    CameraQualityEffect(resolution) {
        quality = it
    }

    val qualityLocal = quality
    if (qualityLocal != null && resolution != null) {
        RecordVideoPageContent(
            quality = qualityLocal,
            resolution = resolution,
            onRecordingFinished = onRecordingFinished,
        )
    }
}