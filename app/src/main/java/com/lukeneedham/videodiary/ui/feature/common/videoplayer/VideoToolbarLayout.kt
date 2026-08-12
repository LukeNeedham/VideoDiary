package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton
import com.lukeneedham.videodiary.ui.feature.common.glass.TopScrim
import com.lukeneedham.videodiary.R

/**
 * Layout shared by full-screen video viewers (calendar day, export view): the video fills the
 * space at the top - full width, with its height driven by [videoAspectRatio] - with [topOverlay]
 * floating "glass" controls over its top edge, and a black toolbar below holding [bottomBar]. The
 * video box (and [video]/[topOverlay] slots) is omitted while [videoAspectRatio] is unknown.
 */
@Composable
fun VideoToolbarLayout(
    videoAspectRatio: Float?,
    modifier: Modifier = Modifier,
    topOverlay: @Composable BoxScope.() -> Unit,
    bottomBar: @Composable BoxScope.() -> Unit,
    video: @Composable BoxScope.(aspectRatio: Float) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (videoAspectRatio != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(videoAspectRatio),
                ) {
                    video(videoAspectRatio)
                }

                TopScrim(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .height(140.dp),
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .statusBarsPadding(),
                content = topOverlay,
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
                .navigationBarsPadding(),
            content = bottomBar,
        )
    }
}

@Preview
@Composable
private fun PreviewVideoToolbarLayout() {
    VideoToolbarLayout(
        videoAspectRatio = 1f,
        topOverlay = {
            GlassIconButton(
                iconRes = R.drawable.close,
                contentDescription = "Close",
                onClick = {},
                modifier = Modifier.align(Alignment.TopStart),
            )
        },
        bottomBar = {
            Text(
                text = "Bottom bar",
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
