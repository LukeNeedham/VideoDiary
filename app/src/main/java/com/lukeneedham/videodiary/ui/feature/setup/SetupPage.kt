package com.lukeneedham.videodiary.ui.feature.setup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SetupPage(
    viewModel: SetupViewModel,
    finishSetup: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.onCompleteEventFlow.collect {
            finishSetup()
        }
    }

    SetupPageContent(
        onContinueClick = { resolution, rotation ->
            viewModel.finaliseSetup(resolution, rotation)
        }
    )
}