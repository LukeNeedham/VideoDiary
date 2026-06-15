package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

                // Keep the thumbnail on top until the player has rendered its first
                // frame, so the player's surface (briefly black while preparing)
                // is never shown to the user.
                if (!controller.hasRenderedFirstFrame) {
                    VideoThumbnail(thumbnailFile)
                }
            } else {
                VideoThumbnail(thumbnailFile)
            }
        }
    }
}

@Composable
private fun VideoThumbnail(thumbnailFile: File?) {
    if (thumbnailFile == null) return

    val thumbnail by produceState<Bitmap?>(initialValue = null, thumbnailFile) {
        value = withContext(Dispatchers.IO) {
            BitmapFactory.decodeFile(thumbnailFile.absolutePath)
        }
    }
    thumbnail?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
internal fun PreviewVideoPlayer() {
    VideoPlayer(
        video = Video.PersistedFile(File("")),
        thumbnailFile = null,
        aspectRatio = 1f,
        controller = rememberVideoPlayerController(),
    )
}
