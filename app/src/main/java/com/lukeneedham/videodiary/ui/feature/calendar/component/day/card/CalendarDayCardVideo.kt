package com.lukeneedham.videodiary.ui.feature.calendar.component.day.card

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import java.io.File

@Composable
fun CalendarDayCardVideo(
    video: File,
    videoAspectRatio: Float,
    videoPlayerController: VideoPlayerController,
) {
    var hasPointersDown by remember { mutableStateOf(false) }

    LaunchedEffect(hasPointersDown) {
        if (hasPointersDown) {
            videoPlayerController.temporaryPause()
        } else {
            videoPlayerController.temporaryResume()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        down.consume()
                        hasPointersDown = true

                        val up = waitForUpOrCancellation()
                        up?.consume()
                        hasPointersDown = false
                    }
                }
        ) {
            VideoPlayer(
                video = Video.PersistedFile(video),
                aspectRatio = videoAspectRatio,
                controller = videoPlayerController,
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayCardVideo() {
    CalendarDayCardVideo(
        video = MockDataCalendar.file,
        videoAspectRatio = 1f,
        videoPlayerController = rememberVideoPlayerController(),
    )
}