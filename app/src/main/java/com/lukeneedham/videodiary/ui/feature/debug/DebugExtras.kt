package com.lukeneedham.videodiary.ui.feature.debug

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

/**
 * VideoDiary's own debug tools, passed to the shared library's `DebugPage` as its `extraContent`.
 * The page itself (animation speed, logs, crashes) comes from the library.
 */
@Composable
fun DebugExtras(
    viewModel: DebugViewModel,
) {
    val allowRetakeForPastDays by viewModel.allowRetakeForPastDays.collectAsState()

    DebugExtrasContent(
        onFillWithMockDataClick = viewModel::fillWithMockData,
        allowRetakeForPastDays = allowRetakeForPastDays,
        onAllowRetakeForPastDaysChange = viewModel::setAllowRetakeForPastDays,
        onResyncThumbnailsClick = viewModel::resyncThumbnails,
    )
}
