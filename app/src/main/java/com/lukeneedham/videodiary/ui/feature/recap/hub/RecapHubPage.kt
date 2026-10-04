package com.lukeneedham.videodiary.ui.feature.recap.hub

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.lukeneedham.videodiary.domain.model.SavedRecap
import com.lukeneedham.videodiary.ui.feature.recap.create.RecapCreateTypeSheet

@Composable
fun RecapHubPage(
    viewModel: RecapHubViewModel,
    onMenuClick: () -> Unit,
    onCreateWeekClick: () -> Unit,
    onCreateMonthClick: () -> Unit,
    onCreateYearClick: () -> Unit,
    onCreateCustomClick: () -> Unit,
    onRecapClick: (SavedRecap) -> Unit,
) {
    var showCreateSheet by rememberSaveable { mutableStateOf(false) }

    RecapHubPageContent(
        savedRecaps = viewModel.savedRecaps,
        videoAspectRatio = viewModel.videoAspectRatio,
        onMenuClick = onMenuClick,
        onCreateRecapClick = { showCreateSheet = true },
        onRecapClick = onRecapClick,
        onDeleteClick = viewModel::deleteRecap,
    )

    if (showCreateSheet) {
        RecapCreateTypeSheet(
            onWeekClick = onCreateWeekClick,
            onMonthClick = onCreateMonthClick,
            onYearClick = onCreateYearClick,
            onCustomClick = onCreateCustomClick,
            onDismiss = { showCreateSheet = false },
        )
    }
}
