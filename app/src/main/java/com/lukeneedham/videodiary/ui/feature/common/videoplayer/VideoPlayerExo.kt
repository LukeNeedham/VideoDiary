package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import android.view.TextureView
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.media.VideoPlayerHolder
import org.koin.compose.getKoin

/**
 * Not previewable
 */
@Composable
fun VideoPlayerExo(
    video: Video,
    controller: VideoPlayerController
) {
    val videoPlayerHolder: VideoPlayerHolder = getKoin().get()

    val player = videoPlayerHolder.player

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

            // Stop playback of the previous video
            player.stop()
            player.setVideoTextureView(view)

            view
        },
        update = { view ->
            view.alpha = if (controller.hasRenderedFirstFrame) 1f else 0f
        },
    )
}