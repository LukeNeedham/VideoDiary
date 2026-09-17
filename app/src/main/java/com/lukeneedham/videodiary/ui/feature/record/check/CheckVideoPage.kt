package com.lukeneedham.videodiary.ui.feature.record.check

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lukeneedham.videodiary.ui.media.VideoPlayerHolder
import org.koin.compose.getKoin

@Composable
fun CheckVideoPage(
    viewModel: CheckVideoViewModel,
    onKeepExisting: () -> Unit,
    onRetake: () -> Unit,
    onKeepNew: () -> Unit,
) {
    // This page plays two videos at once, each on its own dedicated player - on top of the
    // single app-wide shared player, that's three concurrent video decoders, which is enough to
    // exhaust memory on some devices. The shared player isn't visible here, so free its decoder
    // for the duration; returning to the calendar reconfigures it from scratch regardless.
    val videoPlayerHolder: VideoPlayerHolder = getKoin().get()
    LaunchedEffect(Unit) {
        videoPlayerHolder.player.stop()
    }

    // There's no dedicated close button - backing out (rather than picking a video) keeps the
    // existing one, same as tapping its choose button, and returns straight to the calendar
    // rather than the record page (retaking is its own explicit button).
    BackHandler {
        viewModel.discardNewVideo()
        onKeepExisting()
    }

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
