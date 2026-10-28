package com.lukeneedham.videodiary.ui.feature.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VideoStoragePage(
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<VideoStorageViewModel>()
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
