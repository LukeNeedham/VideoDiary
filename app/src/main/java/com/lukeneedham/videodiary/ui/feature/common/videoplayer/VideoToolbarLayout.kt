package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton
import com.lukeneedham.videodiary.ui.feature.common.glass.TopScrim
import com.lukeneedham.videodiary.ui.feature.common.toolbar.ToolbarPageLayout

/**
 * Layout shared by full-screen video pages (calendar day, recap view, record): a
 * [ToolbarPageLayout] with the [video] filling its top space, with an optional [topOverlay]
 * floating "glass" controls over the video's top edge (with a scrim behind it for legibility,
 * omitted entirely when there's no overlay to show), and [bottomBar] in the black toolbar below.
 */
@Composable
fun VideoToolbarLayout(
    modifier: Modifier = Modifier,
    topOverlay: (@Composable BoxScope.() -> Unit)? = null,
    bottomBar: @Composable BoxScope.() -> Unit,
    video: @Composable BoxScope.(aspectRatio: Float) -> Unit,
) {
    ToolbarPageLayout(
        modifier = modifier,
        bottomBar = bottomBar,
    ) { aspectRatio ->
        Box(modifier = Modifier.fillMaxSize()) {
            video(aspectRatio)
        }

        if (topOverlay != null) {
            TopScrim(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .height(140.dp),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .statusBarsPadding(),
                content = topOverlay,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewVideoToolbarLayout() {
    VideoToolbarLayout(
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
