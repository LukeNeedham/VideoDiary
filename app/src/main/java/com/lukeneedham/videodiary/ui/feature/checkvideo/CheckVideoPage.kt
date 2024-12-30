package com.lukeneedham.videodiary.ui.feature.checkvideo

import androidx.compose.runtime.Composable

@Composable
fun CheckVideoPage(
    viewModel: CheckVideoViewModel,
    onRetakeClick: () -> Unit,
    onAccepted: () -> Unit,
) {
    CheckVideoPageContent(
        onRetakeClick = onRetakeClick,
        onAccepted = {
            viewModel.acceptVideo()
            onAccepted()
        },
        video = viewModel.video,
        videoAspectRatio = viewModel.videoAspectRatio,
    )
}