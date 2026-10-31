package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.theme.AppTheme
import java.io.File

/**
 * Previewable
 */
@Composable
fun VideoPlayer(
    video: Video,
    aspectRatio: Float,
    controller: VideoPlayerController,
    thumbnailFile: File? = null,
    // False when a caller hosts its own persistent VideoPlayerExo elsewhere instead.
    showPlayer: Boolean = true,
    // True when this video must play simultaneously with another (e.g. a side-by-side
    // comparison), rather than sharing the single app-wide player.
    usesDedicatedPlayer: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color = Color.Black)
            .aspectRatio(aspectRatio)
    ) {
        // Don't render the video player in Previews - ExoPlayer will break Previews
        val isPreview = LocalInspectionMode.current
        if (isPreview) {
            VideoPlayerPlaceholder(modifier = Modifier.align(Alignment.Center))
        } else {
            val isCurrent = controller.playingVideo == video

            // Drawn underneath the player. While the player's view is hidden
            // (before its first frame is rendered), this remains visible -
            // avoiding a black flash when switching to the player.
            VideoThumbnail(thumbnailFile)

            if (showPlayer && isCurrent) {
                if (usesDedicatedPlayer) {
                    VideoPlayerExoStandalone(
                        video = video,
                        controller = controller,
                    )
                } else {
                    VideoPlayerExo(
                        video = video,
                        controller = controller,
                    )
                }
            }
        }
    }
}

@Composable
private fun VideoThumbnail(thumbnailFile: File?) {
    if (thumbnailFile == null) return

    AsyncImage(
        model = thumbnailFile,
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        VideoPlayer(
            video = Video.PersistedFile(File("")),
            thumbnailFile = null,
            aspectRatio = 1f,
            controller = rememberVideoPlayerController(),
        )
    }
}
