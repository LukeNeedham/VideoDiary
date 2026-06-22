package com.lukeneedham.videodiary.ui.feature.record.check

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun CheckVideoPage(
    viewModel: CheckVideoViewModel,
    onCancelClick: () -> Unit,
    onRetakeClick: () -> Unit,
    onAccepted: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        val videoAspectRatio = viewModel.videoAspectRatio
        if (videoAspectRatio != null) {
            CheckVideoPageContent(
                onCancelClick = onCancelClick,
                onRetakeClick = onRetakeClick,
                onAccepted = {
                    viewModel.acceptVideo()
                    onAccepted()
                },
                video = viewModel.video,
                videoAspectRatio = videoAspectRatio,
            )
        }
    }
}