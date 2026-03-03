package com.lukeneedham.videodiary.ui.feature.savedvideos

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import com.lukeneedham.videodiary.domain.model.SavedExport

@Composable
fun SavedVideosPage(
    viewModel: SavedVideosViewModel,
    onBack: () -> Unit,
    onVideoSelected: (ExportedVideo) -> Unit,
) {
    val savedVideos by viewModel.savedVideos.collectAsState()

    SavedVideosPageContent(
        savedVideos = savedVideos,
        onBack = onBack,
        onClick = { savedExport ->
            onVideoSelected(
                ExportedVideo(
                    videoFile = savedExport.file,
                    startDate = savedExport.startDate,
                    endDate = savedExport.endDate,
                    dayVideoCount = savedExport.dayVideoCount,
                )
            )
        },
        onDelete = {
            viewModel.deleteExport(it)
        }
    )
}
