package com.lukeneedham.videodiary.ui.feature.storage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun VideoStoragePage(
    viewModel: VideoStorageViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    val location by viewModel.location.collectAsState()
    val isRemovableStorageAvailable by viewModel.isRemovableStorageAvailable.collectAsState()
    val context = LocalContext.current

    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.selectCustomFolder(context, uri)
        }
    }

    VideoStoragePageContent(
        location = location,
        isRemovableStorageAvailable = isRemovableStorageAvailable,
        isChangingLocation = viewModel.isChangingLocation,
        error = viewModel.error,
        onLocationSelected = viewModel::selectLocation,
        onSelectCustomFolder = { folderPickerLauncher.launch(null) },
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
