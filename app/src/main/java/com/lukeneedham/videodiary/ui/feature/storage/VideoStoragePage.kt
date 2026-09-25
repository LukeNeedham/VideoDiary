package com.lukeneedham.videodiary.ui.feature.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun VideoStoragePage(
    viewModel: VideoStorageViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    val location by viewModel.location.collectAsState()
    val isRemovableStorageAvailable by viewModel.isRemovableStorageAvailable.collectAsState()

    VideoStoragePageContent(
        location = location,
        isRemovableStorageAvailable = isRemovableStorageAvailable,
        isChangingLocation = viewModel.isChangingLocation,
        error = viewModel.error,
        onLocationSelected = viewModel::selectLocation,
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
