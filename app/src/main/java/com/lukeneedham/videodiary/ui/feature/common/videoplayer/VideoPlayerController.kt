package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class VideoPlayerController {
    var isVolumeOn by mutableStateOf(false)
    var isPlaying by mutableStateOf(true)

    fun toggleVolumeOn() {
        isVolumeOn = !isVolumeOn
    }

    fun toggleIsPlaying() {
        isPlaying = !isPlaying
    }
}