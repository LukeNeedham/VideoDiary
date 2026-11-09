package com.lukeneedham.videodiary.ui.feature.setup.orientation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import com.lukeneedham.videodiary.domain.model.Orientation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetupSelectOrientationPage(
    onContinue: () -> Unit,
    setOrientation: (Orientation) -> Unit,
    /** Hands up what the primary button of the setup page does for this page. */
    registerSave: (save: () -> Unit) -> Unit,
) {
    val viewModel = koinViewModel<SetupSelectOrientationViewModel>()

    SideEffect {
        registerSave(viewModel::saveSettings)
    }

    LaunchedEffect(Unit) {
        viewModel.onSavedEventFlow.collect {
            onContinue()
        }
    }

    val selectedOrientation = viewModel.orientation

    LaunchedEffect(selectedOrientation) {
        setOrientation(selectedOrientation)
    }

    SetupSelectOrientationPageContent(
        options = viewModel.options,
        selectedOption = selectedOrientation,
        setSelectedOption = viewModel::onOrientationChange,
    )
}
