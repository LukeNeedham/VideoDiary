package com.lukeneedham.videodiary.ui.feature.setup.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetupSelectStoragePage(
    onContinue: () -> Unit,
    /** Hands up what the primary button of the setup page does for this page. */
    registerSave: (save: () -> Unit) -> Unit,
) {
    val viewModel = koinViewModel<SetupSelectStorageViewModel>()

    SideEffect {
        registerSave(viewModel::saveSettings)
    }

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
