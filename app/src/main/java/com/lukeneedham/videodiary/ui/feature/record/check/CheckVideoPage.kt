package com.lukeneedham.videodiary.ui.feature.record.check

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    // Backing out (rather than picking a video) keeps the existing one, same as tapping its
    // choose button, and returns straight to the calendar rather than the record page (retaking
    // is its own explicit button). Used by both the system back and the toolbar's back button.
    val onBack = {
        viewModel.discardNewVideo()
        onKeepExisting()
    }
    BackHandler(onBack = onBack)

    CheckVideoPageContent(
        existingVideo = viewModel.existingVideo,
        newVideo = viewModel.newVideo,
        onBack = onBack,
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
