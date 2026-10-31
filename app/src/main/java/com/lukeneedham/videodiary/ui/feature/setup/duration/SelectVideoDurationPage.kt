package com.lukeneedham.videodiary.ui.feature.setup.duration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SelectVideoDurationPage(
    onContinue: () -> Unit,
    /** Hands up what the primary button of the setup page does for this page. */
    registerSave: (save: () -> Unit) -> Unit,
) {
    val viewModel = koinViewModel<SelectVideoDurationViewModel>()

    SideEffect {
        registerSave(viewModel::saveSettings)
    }

    LaunchedEffect(Unit) {
        viewModel.onSavedEventFlow.collect {
            onContinue()
        }
    }

    SelectVideoDurationPageContent(
        seconds = viewModel.durationSeconds,
        setSeconds = viewModel::setDuration,
    )
}
