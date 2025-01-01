package com.lukeneedham.videodiary.ui.feature.setup.orientation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.domain.model.Orientation

@Composable
fun SetupSelectOrientationPage(
    viewModel: SetupSelectOrientationViewModel,
    onContinue: () -> Unit,
    setOrientation: (Orientation) -> Unit,
) {
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
        onContinue = {
            viewModel.saveSettings()
        }
    )
}