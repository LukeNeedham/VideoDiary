package com.lukeneedham.videodiary.ui.feature.setup.duration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.ui.navigation.setup.PagerAction

@Composable
fun SelectVideoDurationPage(
    viewModel: SelectVideoDurationViewModel,
    onContinue: () -> Unit,
    reportBottomAction: (PagerAction?) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.onSavedEventFlow.collect {
            onContinue()
        }
    }

    SelectVideoDurationPageContent(
        seconds = viewModel.durationSeconds,
        setSeconds = viewModel::setDuration,
        onContinue = viewModel::saveSettings,
        reportBottomAction = reportBottomAction,
    )
}
