package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.exoplayer.ExoPlayer
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.theme.AppTheme
import java.io.File

/**
 * Like [VideoPlayerExo], but creates and owns a dedicated [ExoPlayer] rather than using the
 * single app-wide shared player. Used where multiple videos must play at the same time, such as
 * the side-by-side comparison on the check video page.
 */
@Composable
fun VideoPlayerExoStandalone(
    video: Video,
    controller: VideoPlayerController,
    modifier: Modifier = Modifier,
) {
    // ExoPlayer cannot run in a preview: a still frame stands in for the video
    if (LocalInspectionMode.current) {
        VideoPlayerPlaceholder(modifier = modifier)
        return
    }

    val player = rememberStandaloneExoPlayer()

    VideoPlayerExoContent(
        video = video,
        controller = controller,
        player = player,
        stopPlayerBeforeAttaching = false,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        VideoPlayerExoStandalone(
            video = Video.PersistedFile(File("")),
            controller = rememberVideoPlayerController(),
        )
    }
}
