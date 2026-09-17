package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

/**
 * Creates an [ExoPlayer] dedicated to the calling composable (as opposed to the single app-wide
 * shared player), releasing it when the composable leaves composition.
 */
@Composable
fun rememberStandaloneExoPlayer(): ExoPlayer {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }

    return player
}
