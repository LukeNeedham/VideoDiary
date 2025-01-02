package com.lukeneedham.videodiary.ui.feature.record.film.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.record.common.RecordVideoActionBarSize
import com.lukeneedham.videodiary.ui.feature.record.film.MockDataRecordVideo
import com.lukeneedham.videodiary.ui.feature.record.film.RecordingState

@Composable
fun RecordVideoActionBar(
    state: RecordingState,
    onRecordClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(RecordVideoActionBarSize.height)
            .background(Color.Black)
            .padding(horizontal = 10.dp)
    ) {
        // Close button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(50.dp)
                .clickable { onCloseClick() }
        ) {
            Image(
                painter = painterResource(R.drawable.close),
                contentDescription = "Close",
                colorFilter = ColorFilter.tint(color = Color.White),
                modifier = Modifier.size(30.dp)
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f)
        ) {
            when (state) {
                is RecordingState.Failed -> {
                    val text = "Error! ${state.errorCode} : ${state.exception}"
                    Text(
                        text = text,
                        color = Color.White,
                    )
                }

                RecordingState.Ready -> {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape,
                            )
                            .padding(3.dp)
                            .background(
                                color = Color.Red,
                                shape = CircleShape,
                            )
                            .clickable {
                                onRecordClick()
                            }
                    )
                }

                is RecordingState.Recording -> {
                    val millis = state.duration.inWholeMilliseconds
                    val seconds = millis / 1000f
                    val format = "%.1f"
                    val text = format.format(seconds)
                    Text(
                        text = text,
                        color = Color.White,
                    )
                }

                is RecordingState.Success ->
                    Text(
                        text = "Done",
                        color = Color.White,
                    )
            }
        }

        // Mirror for close button - to keep the other content centered
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        )
    }
}

@Preview
@Composable
internal fun PreviewRecordVideoActionBar() {
    RecordVideoActionBar(
        state = MockDataRecordVideo.recordingState,
        onRecordClick = {},
        onCloseClick = {},
    )
}