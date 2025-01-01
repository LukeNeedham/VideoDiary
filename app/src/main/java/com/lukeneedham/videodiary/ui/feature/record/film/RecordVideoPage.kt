package com.lukeneedham.videodiary.ui.feature.record.film

import android.net.Uri
import androidx.camera.video.Quality
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lukeneedham.videodiary.ui.feature.common.camera.CameraQualityEffect

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
    val videoDurationMillis = viewModel.videoDurationMillis
    if (qualityLocal != null && resolution != null && videoDurationMillis != null) {
        RecordVideoPageContent(
            videoDurationMillis = videoDurationMillis,
            quality = qualityLocal,
            resolution = resolution,
            onRecordingFinished = onRecordingFinished,
        )
    }
}