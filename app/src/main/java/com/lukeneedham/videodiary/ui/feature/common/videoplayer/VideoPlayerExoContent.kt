package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import android.view.TextureView
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.lukeneedham.videodiary.domain.model.Video

/**
 * Shared rendering/sync logic for playing [video] on [player] according to [controller]'s state.
 *
 * [stopPlayerBeforeAttaching] should be true when [player] may already be playing a different
 * video (the shared app-wide player), so it's stopped before this composable claims its texture
 * view; it's unnecessary (but harmless) for a freshly created, dedicated player.
 */
// lint:ignore PRV-7: needs a real ExoPlayer; VideoPlayerExo and VideoPlayerExoStandalone, which call it, have previews with a placeholder
@Composable
internal fun VideoPlayerExoContent(
    video: Video,
    controller: VideoPlayerController,
    player: ExoPlayer,
    stopPlayerBeforeAttaching: Boolean,
    modifier: Modifier = Modifier,
) {
    DisposableEffect(player, controller) {
        val listener = object : Player.Listener {
            override fun onRenderedFirstFrame() {
                controller.hasRenderedFirstFrame = true
            }
        }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }

    LaunchedEffect(video) {
        controller.hasRenderedFirstFrame = false

        val videoMediaItem = when (video) {
            is Video.MediaStore -> MediaItem.fromUri(video.uri)
            is Video.PersistedFile -> MediaItem.fromUri(video.file.toUri())
        }

        player.apply {
            setMediaItem(videoMediaItem)
            prepare()
            play()
        }
    }

    // Synchronise controller state to player
    val controllerIsPlaying = controller.isPlaying
    LaunchedEffect(controllerIsPlaying) {
        player.playWhenReady = controllerIsPlaying
    }

    val controllerIsVolumeOn = controller.isVolumeOn
    LaunchedEffect(controllerIsVolumeOn) {
        player.volume = if (controllerIsVolumeOn) 1f else 0f
    }

    AndroidView(
        modifier = modifier,
        factory = {
            val view = TextureView(it)
            view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )

            // Hide the texture until the first frame is rendered, so the thumbnail
            // drawn underneath remains visible instead of a black surface.
            // AndroidView content is composited above Compose-drawn siblings, so
            // hiding/showing this view (rather than the thumbnail) is what's needed
            // to avoid the flash.
            view.alpha = 0f

            if (stopPlayerBeforeAttaching) {
                // Stop playback of the previous video
                player.stop()
            }
            player.setVideoTextureView(view)

            view
        },
        update = { view ->
            view.alpha = if (controller.hasRenderedFirstFrame) 1f else 0f
        },
    )
}
