package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.feature.common.toolbar.ToolbarPageLayout

/**
 * Layout shared by full-screen video pages (calendar day, recap view, record): a
 * [ToolbarPageLayout] with the [video] filling its top space and [bottomBar] in the
 * black toolbar below.
 */
@Composable
fun VideoToolbarLayout(
    modifier: Modifier = Modifier,
    bottomBar: @Composable BoxScope.() -> Unit,
    video: @Composable BoxScope.(aspectRatio: Float) -> Unit,
) {
    ToolbarPageLayout(
        modifier = modifier,
        bottomBar = bottomBar,
    ) { aspectRatio ->
        video(aspectRatio)
    }
}

@Preview
@Composable
private fun PreviewVideoToolbarLayout() {
    VideoToolbarLayout(
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
