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
 * Layout for pages that don't show a video, but still share the video pages' proportions: the
 * [content] fills the space at the top - full width, with its height driven by the video aspect
 * ratio exactly as a video's would be - and the black toolbar (with the back button) fills the
 * space below, so the toolbar sits in the same place on every page.
 *
 * The layout looks up the aspect ratio itself (from [VideoResolutionRepository]), so pages don't
 * need to know about it. [content] is omitted while the aspect ratio is loading.
 */
@Composable
fun ToolbarPageLayout(
    canGoBack: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val isPreview = LocalInspectionMode.current
    var videoAspectRatio: Float? by remember {
        mutableStateOf(if (isPreview) PREVIEW_ASPECT_RATIO else null)
    }
    if (!isPreview) {
        val videoResolutionRepository = koinInject<VideoResolutionRepository>()
        LaunchedEffect(videoResolutionRepository) {
            videoAspectRatio = videoResolutionRepository.getAspectRatio()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        val aspectRatio = videoAspectRatio
        if (aspectRatio != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio),
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
        canGoBack = true,
        onBack = {},
    ) {
        Text(text = "Content", modifier = Modifier.align(Alignment.Center))
    }
}
