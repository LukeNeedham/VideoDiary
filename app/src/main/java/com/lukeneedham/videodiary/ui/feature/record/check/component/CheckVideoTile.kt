package com.lukeneedham.videodiary.ui.feature.record.check.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import com.lukeneedham.videodiary.ui.theme.AppTheme
import java.io.File

/**
 * One half of the check video comparison: a playing [video] that pauses - alongside its
 * counterpart, via [onPress]/[onRelease] - for as long as the user holds a finger down on it.
 */
@Composable
fun CheckVideoTile(
    video: Video,
    controller: VideoPlayerController,
    aspectRatio: Float,
    onPress: () -> Unit,
    onRelease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .pointerInput(onPress, onRelease) {
                detectTapGestures(
                    onPress = {
                        onPress()
                        tryAwaitRelease()
                        onRelease()
                    },
                )
            },
    ) {
        VideoPlayer(
            video = video,
            aspectRatio = aspectRatio,
            controller = controller,
            usesDedicatedPlayer = true,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        CheckVideoTile(
            video = Video.PersistedFile(File("")),
            controller = rememberVideoPlayerController(),
            aspectRatio = 9f / 16f,
            onPress = {},
            onRelease = {},
        )
    }
}
