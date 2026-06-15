package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.domain.util.logger.Logger
import java.io.File

/**
 * Previewable
 */
@Composable
fun VideoPlayer(
    video: Video,
    aspectRatio: Float,
    controller: VideoPlayerController,
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
            Image(
                painter = painterResource(R.drawable.preview_video),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            val isCurrent = controller.playingVideo == video

            if (isCurrent) {
                VideoPlayerExo(
                    video = video,
                    controller = controller,
                )
            } else {
                // todo: render the thumbnail for this video,
                //  which should be the first frame of the video.
                //  Really this should be saved on disk next to the video,
                //  so we can load it really quickly.
                Text(text = "thumbnail", color = Color.White)
            }
        }
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