package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberVideoPlayerController(): VideoPlayerController {
    return remember { VideoPlayerController() }
}