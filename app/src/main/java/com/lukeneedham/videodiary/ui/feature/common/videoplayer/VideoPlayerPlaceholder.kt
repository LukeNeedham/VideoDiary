package com.lukeneedham.videodiary.ui.feature.common.videoplayer

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.theme.AppTheme

/** A still frame in place of a video, for previews: ExoPlayer cannot run in one. */
@Composable
internal fun VideoPlayerPlaceholder(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.preview_video),
        contentDescription = null,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        VideoPlayerPlaceholder()
    }
}
