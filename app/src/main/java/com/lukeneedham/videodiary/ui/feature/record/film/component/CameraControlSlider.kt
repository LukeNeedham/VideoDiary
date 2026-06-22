package com.lukeneedham.videodiary.ui.feature.record.film.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R

@Composable
fun CameraControlSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    iconRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    val thumbRadiusDp = 10.dp
    val trackWidthDp = 4.dp
    val touchTargetWidthDp = 44.dp
    val thumbRadiusPx = with(LocalDensity.current) { thumbRadiusDp.toPx() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(20.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Canvas(
            modifier = Modifier
                .weight(1f)
                .width(touchTargetWidthDp)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        val trackTop = thumbRadiusPx
                        val trackBottom = size.height - thumbRadiusPx
                        val trackHeight = trackBottom - trackTop

                        fun valueFromY(y: Float): Float {
                            return (1f - ((y - trackTop) / trackHeight)).coerceIn(0f, 1f)
                        }

                        onValueChange(valueFromY(down.position.y))
                        down.consume()

                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull() ?: break
                            if (!change.pressed) break
                            change.consume()
                            onValueChange(valueFromY(change.position.y))
                        }
                    }
                }
        ) {
            val centerX = size.width / 2
            val trackTop = thumbRadiusPx
            val trackBottom = size.height - thumbRadiusPx
            val trackHeight = trackBottom - trackTop
            val trackWidthPx = trackWidthDp.toPx()

            drawRoundRect(
                color = Color.White.copy(alpha = 0.3f),
                topLeft = Offset(centerX - trackWidthPx / 2, trackTop),
                size = Size(trackWidthPx, trackHeight),
                cornerRadius = CornerRadius(trackWidthPx / 2),
            )

            val thumbY = trackTop + trackHeight * (1f - value)
            drawCircle(
                color = Color.White,
                radius = thumbRadiusPx,
                center = Offset(centerX, thumbY),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCameraControlSlider() {
    Column(
        modifier = Modifier.background(Color.DarkGray),
    ) {
        CameraControlSlider(
            value = 0.5f,
            onValueChange = {},
            iconRes = R.drawable.brightness,
            contentDescription = "Brightness",
            modifier = Modifier.height(200.dp),
        )
    }
}
