package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import android.view.TextureView
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.media.VideoPlayerPool

@Composable
fun VideoPlayer(
    video: Video,
    aspectRatio: Float,
    controller: VideoPlayerController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val videoPlayer = remember {
        val videoMediaItem = when (video) {
            is Video.MediaStore -> MediaItem.fromUri(video.uri)
            is Video.PersistedFile -> MediaItem.fromUri(video.file.toUri())
        }

        ExoPlayer.Builder(context).build().apply {
            setMediaItem(videoMediaItem)
            repeatMode = Player.REPEAT_MODE_ONE
            prepare()
            play()
        }
    }

    val controllerIsPlaying = controller.isPlaying
    LaunchedEffect(controllerIsPlaying) {
        videoPlayer.playWhenReady = controllerIsPlaying
    }

    val controllerIsVolumeOn = controller.isVolumeOn
    LaunchedEffect(controllerIsVolumeOn) {
        videoPlayer.volume = if (controllerIsVolumeOn) 1f else 0f
    }

    DisposableEffect(videoPlayer) {
        VideoPlayerPool.addPlayer(videoPlayer)
        onDispose {
            VideoPlayerPool.removePlayer(videoPlayer)
            videoPlayer.stop()
            videoPlayer.release()
        }
    }

    Box(
        modifier = modifier
            .background(color = Color.Black)
            .aspectRatio(aspectRatio)
    ) {
        AndroidView(
            factory = {
                val view = TextureView(it)
                view.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                videoPlayer.setVideoTextureView(view)
                view
            }
        )
    }
}