package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.Video
import java.io.File
import kotlin.math.max

@Composable
fun VideoPlayer(
    video: Video,
    aspectRatio: Float,
    controller: VideoPlayerController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.background(color = Color.Black)
    ) {
        // Don't render the video player in Previews - ExoPlayer will break Previews
        val isPreview = LocalInspectionMode.current
        if (isPreview) {
            Box(modifier = Modifier.aspectRatio(aspectRatio)) {
                Image(
                    painter = painterResource(R.drawable.preview_video),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            VideoPlayerAmbientBackground(
                video = video,
                aspectRatio = aspectRatio,
                controller = controller,
                modifier = Modifier.matchParentSize(),
            )
            Box(modifier = Modifier.aspectRatio(aspectRatio)) {
                VideoPlayerExo(
                    video = video,
                    controller = controller,
                )
            }
        }
    }
}

/**
 * Fills the space behind the letterboxed video with a darkened, zoomed-in copy of the same
 * video, blurred on API 31+ (where [Modifier.blur] has an effect; on older devices it's a
 * no-op, so this still shows a darkened, cropped backdrop instead of flat black bars).
 *
 * Uses a second player instance, since a single ExoPlayer can only render to one surface.
 */
@Composable
private fun VideoPlayerAmbientBackground(
    video: Video,
    aspectRatio: Float,
    controller: VideoPlayerController,
    modifier: Modifier = Modifier,
) {
    val ambientController = remember { VideoPlayerController() }
    LaunchedEffect(controller.isPlaying) {
        if (controller.isPlaying) ambientController.temporaryResume() else ambientController.temporaryPause()
    }

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier.clipToBounds(),
    ) {
        val boxAspectRatio = maxWidth.value / maxHeight.value
        // Scale factor that turns the letterboxed ("fit") video size into one that covers the
        // full background area ("crop"), whichever dimension is the limiting one.
        val fitToBoxRatio = aspectRatio / boxAspectRatio
        val coverScale = max(fitToBoxRatio, 1f / fitToBoxRatio)

        Box(
            modifier = Modifier
                .aspectRatio(aspectRatio)
                .scale(coverScale)
                .blur(60.dp)
        ) {
            VideoPlayerExo(
                video = video,
                controller = ambientController,
            )
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.55f))
        )
    }
}

@Preview
@Composable
internal fun PreviewVideoPlayer() {
    VideoPlayer(
        video = Video.PersistedFile(File("")),
        aspectRatio = 1f,
        controller = rememberVideoPlayerController(),
    )
}
