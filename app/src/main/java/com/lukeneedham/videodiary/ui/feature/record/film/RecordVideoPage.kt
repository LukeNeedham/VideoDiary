package com.lukeneedham.videodiary.ui.feature.record.film

import android.net.Uri
import androidx.camera.video.Quality
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lukeneedham.videodiary.ui.feature.common.camera.CameraQualityEffect
import java.time.LocalDate
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RecordVideoPage(
    date: LocalDate,
    onCheckVideo: (videoContentUri: Uri) -> Unit,
    onVideoSaved: () -> Unit,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<RecordVideoViewModel> {
        parametersOf(date)
    }

    var quality: Quality? by remember { mutableStateOf(null) }

    val resolution = viewModel.resolution

    CameraQualityEffect(resolution) {
        quality = it
    }

    RecordVideoPageContent(
        videoDurationMillis = viewModel.videoDurationMillis,
        resolution = resolution,
        quality = quality,
        onRecordingFinished = { videoContentUri ->
            if (viewModel.hasExistingVideo) {
                onCheckVideo(videoContentUri)
            } else {
                viewModel.persistVideoDirectly(videoContentUri)
                onVideoSaved()
            }
        },
        onBack = onBack,
    )
}