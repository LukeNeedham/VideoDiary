package com.lukeneedham.videodiary.ui.feature.setup.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SetupSelectStoragePage(
    viewModel: SetupSelectStorageViewModel,
    onContinue: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.onSavedEventFlow.collect {
            onContinue()
        }
    }

    SetupSelectStoragePageContent(
        options = viewModel.options,
        selectedOption = viewModel.location,
        setSelectedOption = viewModel::onLocationChange,
        error = viewModel.error,
    )
}
