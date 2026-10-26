package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.exoplayer.ExoPlayer
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.media.VideoPlayerHolder
import com.lukeneedham.videodiary.ui.theme.AppTheme
import java.io.File
import org.koin.compose.getKoin

/**
 * Uses the single app-wide shared [ExoPlayer], so only one [VideoPlayerExo] should be visibly
 * playing at a time. For simultaneous playback (e.g. comparing two videos side by side), use
 * [VideoPlayerExoStandalone] instead.
 */
@Composable
fun VideoPlayerExo(
    video: Video,
    controller: VideoPlayerController,
    modifier: Modifier = Modifier,
) {
    // ExoPlayer cannot run in a preview: a still frame stands in for the video
    if (LocalInspectionMode.current) {
        VideoPlayerPlaceholder(modifier = modifier)
        return
    }

    val videoPlayerHolder: VideoPlayerHolder = getKoin().get()
    val player = videoPlayerHolder.player

    VideoPlayerExoContent(
        video = video,
        controller = controller,
        player = player,
        stopPlayerBeforeAttaching = true,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        VideoPlayerExo(
            video = Video.PersistedFile(File("")),
            controller = rememberVideoPlayerController(),
        )
    }
}
