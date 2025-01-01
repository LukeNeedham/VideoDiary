package com.lukeneedham.videodiary.ui.feature.record.check

import androidx.compose.runtime.Composable

@Composable
fun CheckVideoPage(
    viewModel: CheckVideoViewModel,
    onCancelClick: () -> Unit,
    onRetakeClick: () -> Unit,
    onAccepted: () -> Unit,
) {
    CheckVideoPageContent(
        onCancelClick = onCancelClick,
        onRetakeClick = onRetakeClick,
        onAccepted = {
            viewModel.acceptVideo()
            onAccepted()
        },
        video = viewModel.video,
        videoAspectRatio = viewModel.videoAspectRatio,
    )
}