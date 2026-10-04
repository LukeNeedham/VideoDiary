package com.lukeneedham.videodiary.ui.feature.common.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R

/**
 * Layout for pages that don't show a video, but still share the video pages' proportions: the
 * [content] fills the space at the top - full width, with its height driven by [videoAspectRatio]
 * exactly as a video's would be - and the black toolbar (with the back button) fills the space
 * below, so the toolbar sits in the same place on every page. [content] is omitted while
 * [videoAspectRatio] is unknown.
 */
@Composable
fun ToolbarPageLayout(
    videoAspectRatio: Float?,
    canGoBack: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (videoAspectRatio != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(videoAspectRatio),
                content = content,
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
                .navigationBarsPadding(),
        ) {
            if (canGoBack) {
                FlatIconButton(
                    iconRes = R.drawable.back,
                    contentDescription = "Back",
                    onClick = onBack,
                    selected = true,
                    size = 44.dp,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewToolbarPageLayout() {
    ToolbarPageLayout(
        videoAspectRatio = 9f / 16f,
        canGoBack = true,
        onBack = {},
    ) {
        Text(text = "Content", modifier = Modifier.align(Alignment.Center))
    }
}
