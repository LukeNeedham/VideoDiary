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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import org.koin.compose.koinInject

private const val PREVIEW_ASPECT_RATIO = 9f / 16f

/**
 * Shared page layout: the [content] fills the space at the top - full width, with its height
 * driven by the video aspect ratio - and a black toolbar holding [bottomBar] fills the space
 * below, so the toolbar sits in the same place on every page, whether or not the page shows a
 * video. [content] is given the aspect ratio, for pages that show a video in it.
 *
 * The layout looks up the aspect ratio itself (from [VideoResolutionRepository]), so pages don't
 * need to know about it. Nothing is rendered until the aspect ratio is known (normally from the
 * first frame, since it's cached at startup), so the layout never shifts as it loads.
 */
@Composable
fun ToolbarPageLayout(
    modifier: Modifier = Modifier,
    bottomBar: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.(aspectRatio: Float) -> Unit,
) {
    val videoAspectRatio = rememberVideoAspectRatio() ?: return

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(videoAspectRatio),
        ) {
            content(videoAspectRatio)
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

/** A [ToolbarPageLayout] whose black toolbar holds just a back button (if [canGoBack]). */
@Composable
fun ToolbarPageLayout(
    canGoBack: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.(aspectRatio: Float) -> Unit,
) {
    ToolbarPageLayout(
        modifier = modifier,
        bottomBar = {
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
        },
        content = content,
    )
}

@Preview
@Composable
private fun PreviewToolbarPageLayout() {
    ToolbarPageLayout(
        canGoBack = true,
        onBack = {},
    ) {
        Text(text = "Content", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun rememberVideoAspectRatio(): Float? {
    if (LocalInspectionMode.current) return PREVIEW_ASPECT_RATIO

    val repository = koinInject<VideoResolutionRepository>()
    var aspectRatio: Float? by remember { mutableStateOf(repository.cachedAspectRatio) }
    if (aspectRatio == null) {
        LaunchedEffect(repository) {
            aspectRatio = repository.getAspectRatio()
        }
    }
    return aspectRatio
}
