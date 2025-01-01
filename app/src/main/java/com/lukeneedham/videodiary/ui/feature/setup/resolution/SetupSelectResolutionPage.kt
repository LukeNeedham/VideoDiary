package com.lukeneedham.videodiary.ui.feature.setup.resolution

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SetupSelectResolutionPage(
    viewModel: SetupSelectResolutionViewModel,
    onContinue: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.onSavedEventFlow.collect {
            onContinue()
        }
    }

    SetupPageContent(
        onContinueClick = { resolution, rotation ->
            viewModel.saveSettings(resolution, rotation)
        }
    )
}