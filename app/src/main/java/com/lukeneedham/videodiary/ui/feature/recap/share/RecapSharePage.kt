package com.lukeneedham.videodiary.ui.feature.recap.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.lukeneedham.videodiary.domain.model.ShareRequest

@Composable
fun RecapSharePage(
    viewModel: RecapShareViewModel,
    canGoBack: Boolean,
    onBack: () -> Unit,
    share: (ShareRequest) -> Unit,
) {
    LaunchedEffect(viewModel) {
        viewModel.onShareFlow.collect {
            share(it)
        }
    }

    RecapSharePageContent(
        state = viewModel.state,
        includeDateStamp = viewModel.includeDateStamp,
        onIncludeDateStampChange = viewModel::onIncludeDateStampChange,
        onCreateClick = viewModel::startExport,
        onCancelClick = viewModel::cancelExport,
        onRetryClick = viewModel::retryAfterFailure,
        onShareClick = viewModel::shareClicked,
        canGoBack = canGoBack,
        onBack = onBack,
    )
}
