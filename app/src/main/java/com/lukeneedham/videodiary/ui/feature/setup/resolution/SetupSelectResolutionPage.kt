package com.lukeneedham.videodiary.ui.feature.setup.resolution

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.ui.navigation.setup.PagerAction

@Composable
fun SetupSelectResolutionPage(
    viewModel: SetupSelectResolutionViewModel,
    onContinue: () -> Unit,
    reportBottomAction: (PagerAction?) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.onSavedEventFlow.collect {
            onContinue()
        }
    }

    SetupPageContent(
        onContinueClick = { resolution, rotation ->
            viewModel.saveSettings(resolution, rotation)
        },
        reportBottomAction = reportBottomAction,
    )
}
