package com.lukeneedham.videodiary.ui.feature.exportdiary.view

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import com.lukeneedham.videodiary.domain.model.ShareRequest

@Composable
fun ExportDiaryViewPage(
    viewModel: ExportDiaryViewViewModel,
    exportedVideo: ExportedVideo,
    share: (ShareRequest) -> Unit,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    ExportDiaryViewPageContent(
        share = share,
        videoAspectRatio = viewModel.videoAspectRatio,
        exportedVideo = exportedVideo,
        canGoBack = canGoBack,
        onBack = onBack,
    )
}