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
    onKeepExisting: () -> Unit,
    onRetake: () -> Unit,
    onKeepNew: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        val videoAspectRatio = viewModel.videoAspectRatio
        if (videoAspectRatio != null) {
            CheckVideoPageContent(
                existingVideo = viewModel.existingVideo,
                newVideo = viewModel.newVideo,
                videoAspectRatio = videoAspectRatio,
                onCloseClick = {
                    viewModel.discardNewVideo()
                    onKeepExisting()
                },
                onRetakeClick = {
                    viewModel.discardNewVideo()
                    onRetake()
                },
                onExistingVideoSelected = {
                    viewModel.discardNewVideo()
                    onKeepExisting()
                },
                onNewVideoSelected = {
                    viewModel.keepNewVideo()
                    onKeepNew()
                },
            )
        }
    }
}
