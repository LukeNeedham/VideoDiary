package com.lukeneedham.videodiary.ui.feature.record.check.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Video
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayer
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.theme.Typography

/**
 * One half of the check video comparison: a [label] above a playing [video], which the user can
 * tap to select. Pressing and holding (without releasing) instead invokes [onPress]/[onRelease] -
 * used to pause both videos being compared while the user is holding down on either.
 */
@Composable
fun CheckVideoTile(
    label: String,
    video: Video,
    controller: VideoPlayerController,
    aspectRatio: Float,
    onSelected: () -> Unit,
    onPress: () -> Unit,
    onRelease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = label,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = Typography.Size.extraSmall,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(onSelected, onPress, onRelease) {
                    detectTapGestures(
                        onPress = {
                            onPress()
                            tryAwaitRelease()
                            onRelease()
                        },
                        // Providing this makes detectTapGestures distinguish a long press
                        // from a tap (onTap below then only fires for a genuine tap).
                        onLongPress = {},
                        onTap = { onSelected() },
                    )
                },
        ) {
            VideoPlayer(
                video = video,
                aspectRatio = aspectRatio,
                controller = controller,
                usesDedicatedPlayer = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
