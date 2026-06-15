package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.domain.util.logger.Logger
import java.io.File

class VideoPlayerController {
    var playingVideo: Video? by mutableStateOf(null)

    /**
     * Pauses the video in a temporary way -
     * as opposed to [isTogglePaused], which is a toggle.
     * This is used for the functionality where a video should be pause only while it is 'held'
     * by the user holding their finger on the video.
     */
    private var isTemporaryPaused by mutableStateOf(false)
    var isTogglePaused by mutableStateOf(false)

    var isVolumeOn by mutableStateOf(false)

    val isPlaying by derivedStateOf {
        !isTemporaryPaused && !isTogglePaused
    }

    fun toggleVolumeOn() {
        isVolumeOn = !isVolumeOn
    }

    fun toggleIsPlaying() {
        isTogglePaused = !isTogglePaused
    }

    fun temporaryPause() {
        isTemporaryPaused = true
    }

    fun temporaryResume() {
        isTemporaryPaused = false
    }
}