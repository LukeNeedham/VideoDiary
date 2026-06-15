package com.lukeneedham.videodiary.ui.media

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class VideoPlayerHolder(
    context: Context,
) {
    /** We use a single player throughout the whole app */
    val player = ExoPlayer.Builder(context).build().apply {
        repeatMode = Player.REPEAT_MODE_ONE
    }

    private var playerPlayingBeforeGlobalPause = false

    fun onAppPause() {
        playerPlayingBeforeGlobalPause = player.isPlaying
        player.pause()
    }

    fun onAppResume() {
        if (playerPlayingBeforeGlobalPause) {
            player.play()
        }
    }
}