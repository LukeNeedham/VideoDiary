package com.lukeneedham.videodiary.ui.feature.exportdiary

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.ShareRequest

@Composable
fun ExportDiaryPage(
    viewModel: ExportDiaryViewModel,
    share: (ShareRequest) -> Unit,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    ExportDiaryPageContent(
        share = share,
        canGoBack = canGoBack,
        onBack = onBack,
        videoCount = viewModel.videoCount,
        exportState = viewModel.exportState,
        export = viewModel::export,
        videoAspectRatio = viewModel.videoAspectRatio
    )
}