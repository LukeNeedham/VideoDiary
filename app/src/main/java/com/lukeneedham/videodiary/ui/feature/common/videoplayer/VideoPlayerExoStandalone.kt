package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.exoplayer.ExoPlayer
import com.lukeneedham.videodiary.domain.model.Video

/**
 * Like [VideoPlayerExo], but creates and owns a dedicated [ExoPlayer] rather than using the
 * single app-wide shared player. Used where multiple videos must play at the same time, such as
 * the side-by-side comparison on the check video page.
 */
// lint:ignore PRV-7: creates a real ExoPlayer, so it cannot be previewed
@Composable
fun VideoPlayerExoStandalone(
    video: Video,
    controller: VideoPlayerController,
    modifier: Modifier = Modifier,
) {
    val player = rememberStandaloneExoPlayer()

    VideoPlayerExoContent(
        video = video,
        controller = controller,
        player = player,
        stopPlayerBeforeAttaching = false,
        modifier = modifier,
    )
}
