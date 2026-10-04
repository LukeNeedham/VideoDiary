package com.lukeneedham.videodiary.ui.feature.recap.hub

import androidx.compose.runtime.Composable
import com.lukeneedham.videodiary.domain.model.SavedRecap

@Composable
fun RecapHubPage(
    viewModel: RecapHubViewModel,
    onMenuClick: () -> Unit,
    onCreateRecapClick: () -> Unit,
    onRecapClick: (SavedRecap) -> Unit,
) {
    RecapHubPageContent(
        savedRecaps = viewModel.savedRecaps,
        videoAspectRatio = viewModel.videoAspectRatio,
        onMenuClick = onMenuClick,
        onCreateRecapClick = onCreateRecapClick,
        onRecapClick = onRecapClick,
        onDeleteClick = viewModel::deleteRecap,
    )
}
