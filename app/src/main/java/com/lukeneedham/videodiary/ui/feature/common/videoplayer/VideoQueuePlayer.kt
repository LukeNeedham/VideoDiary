package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import android.view.TextureView
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.media.VideoPlayerHolder
import org.koin.compose.getKoin
import java.io.File

/**
 * Plays [videoFiles] back-to-back, automatically advancing to the next one and looping back to
 * the start - used to browse a recap without stitching its videos into a single file first.
 *
 * Uses the single app-wide shared [ExoPlayer] like [VideoPlayerExo], so only one of these (or a
 * [VideoPlayer]) should be visibly playing at a time.
 */
@Composable
fun VideoQueuePlayer(
    videoFiles: List<File>,
    aspectRatio: Float,
    controller: VideoPlayerController,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(color = Color.Black)
            .aspectRatio(aspectRatio)
    ) {
        // Don't render the player in Previews - ExoPlayer will break Previews
        if (LocalInspectionMode.current) {
            Image(
                painter = painterResource(R.drawable.preview_video),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            VideoQueuePlayerExo(
                videoFiles = videoFiles,
                controller = controller,
            )
        }
    }
}

@Composable
private fun VideoQueuePlayerExo(
    videoFiles: List<File>,
    controller: VideoPlayerController,
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
            // This is the single app-wide player, so leaving this screen must stop it (otherwise
            // it keeps playing - audio and all - behind whatever's navigated to next), and restore
            // the default mode expected by single-video playback elsewhere in the app (e.g. the
            // calendar day view).
            player.pause()
            player.repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    LaunchedEffect(videoFiles) {
        controller.hasRenderedFirstFrame = false
        if (videoFiles.isEmpty()) return@LaunchedEffect

        val mediaItems = videoFiles.map { MediaItem.fromUri(it.toUri()) }

        // Stop playback of whatever the shared player was previously showing.
        player.stop()
        player.setMediaItems(mediaItems)
        player.repeatMode = Player.REPEAT_MODE_ALL
        player.prepare()
        player.play()
    }

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

            // Hide the texture until the first frame is rendered, avoiding a black flash.
            view.alpha = 0f

            player.setVideoTextureView(view)

            view
        },
        update = { view ->
            view.alpha = if (controller.hasRenderedFirstFrame) 1f else 0f
        },
    )
}
