package com.lukeneedham.videodiary.ui.feature.record.film

import android.net.Uri
import androidx.camera.video.Quality
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lukeneedham.videodiary.ui.feature.common.camera.CameraQualityEffect

@Composable
fun RecordVideoPage(
    viewModel: RecordVideoViewModel,
    onRecordingFinished: (videoContentUri: Uri) -> Unit,
    onBack: () -> Unit,
) {
    var quality: Quality? by remember { mutableStateOf(null) }

    val resolution = viewModel.resolution

    CameraQualityEffect(resolution) {
        quality = it
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        val qualityLocal = quality
        val videoDurationMillis = viewModel.videoDurationMillis
        if (qualityLocal != null && resolution != null && videoDurationMillis != null) {
            RecordVideoPageContent(
                videoDurationMillis = videoDurationMillis,
                quality = qualityLocal,
                resolution = resolution,
                onRecordingFinished = onRecordingFinished,
                onBack = onBack,
            )
        }
    }
}