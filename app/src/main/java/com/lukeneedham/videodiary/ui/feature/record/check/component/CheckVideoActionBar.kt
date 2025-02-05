package com.lukeneedham.videodiary.ui.feature.record.check.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoControlActionBar
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import com.lukeneedham.videodiary.ui.feature.record.common.RecordVideoActionBarSize

@Composable
fun CheckVideoActionBar(
    videoPlayerController: VideoPlayerController,
    onCancelClick: () -> Unit,
    onRetakeClick: () -> Unit,
    onAccepted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(RecordVideoActionBarSize.height)
    ) {
        val verticalPadding = 3.dp
        Spacer(modifier = Modifier.height(verticalPadding))

        VideoControlActionBar(
            hasVideo = true,
            videoPlayerController = videoPlayerController,
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            ActionButton(
                text = "Cancel",
                onClick = onCancelClick,
            )

            ActionButton(
                text = "Retake",
                onClick = onRetakeClick,
            )

            ActionButton(
                text = "Accept",
                onClick = onAccepted,
            )
        }

        Spacer(modifier = Modifier.height(verticalPadding))
    }
}

@Composable
private fun RowScope.ActionButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        text = text,
        onClick = onClick,
        backgroundColor = Color.White,
        foregroundColor = Color.Black,
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
    )
}

@Preview
@Composable
internal fun PreviewCheckVideoActionBar() {
    Box(
        modifier = Modifier.background(Color.Black)
    ) {
        CheckVideoActionBar(
            onRetakeClick = {},
            onAccepted = {},
            onCancelClick = {},
            videoPlayerController = rememberVideoPlayerController(),
        )
    }
}