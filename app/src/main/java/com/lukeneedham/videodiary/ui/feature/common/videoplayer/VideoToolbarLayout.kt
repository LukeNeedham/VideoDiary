package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

/**
 * Layout shared by full-screen video viewers (calendar day, export view): a black toolbar
 * filling the space above the video, and the video itself below - full width, with its height
 * driven by [videoAspectRatio]. The video box (and [video] slot) is omitted while
 * [videoAspectRatio] is unknown.
 */
@Composable
fun VideoToolbarLayout(
    videoAspectRatio: Float?,
    modifier: Modifier = Modifier,
    toolbar: @Composable BoxScope.() -> Unit,
    video: @Composable BoxScope.(aspectRatio: Float) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black),
            content = toolbar,
        )

        if (videoAspectRatio != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(videoAspectRatio),
            ) {
                video(videoAspectRatio)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewVideoToolbarLayout() {
    VideoToolbarLayout(
        videoAspectRatio = 1f,
        toolbar = {
            Text(
                text = "Toolbar",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center),
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            Text(
                text = "Video",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}
