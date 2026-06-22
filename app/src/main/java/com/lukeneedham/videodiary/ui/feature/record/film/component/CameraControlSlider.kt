package com.lukeneedham.videodiary.ui.feature.record.film.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Row
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
    val trackHeightDp = 4.dp
    val touchTargetHeightDp = 44.dp
    val thumbRadiusPx = with(LocalDensity.current) { thumbRadiusDp.toPx() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(20.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Canvas(
            modifier = Modifier
                .weight(1f)
                .height(touchTargetHeightDp)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        val trackLeft = thumbRadiusPx
                        val trackRight = size.width - thumbRadiusPx
                        val trackWidth = trackRight - trackLeft

                        fun valueFromX(x: Float): Float {
                            return ((x - trackLeft) / trackWidth).coerceIn(0f, 1f)
                        }

                        onValueChange(valueFromX(down.position.x))
                        down.consume()

                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull() ?: break
                            if (!change.pressed) break
                            change.consume()
                            onValueChange(valueFromX(change.position.x))
                        }
                    }
                }
        ) {
            val centerY = size.height / 2
            val trackLeft = thumbRadiusPx
            val trackRight = size.width - thumbRadiusPx
            val trackWidth = trackRight - trackLeft
            val trackHeightPx = trackHeightDp.toPx()

            drawRoundRect(
                color = Color.White.copy(alpha = 0.3f),
                topLeft = Offset(trackLeft, centerY - trackHeightPx / 2),
                size = Size(trackWidth, trackHeightPx),
                cornerRadius = CornerRadius(trackHeightPx / 2),
            )

            val thumbX = trackLeft + trackWidth * value
            drawCircle(
                color = Color.White,
                radius = thumbRadiusPx,
                center = Offset(thumbX, centerY),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCameraControlSlider() {
    Row(
        modifier = Modifier.background(Color.Black),
    ) {
        CameraControlSlider(
            value = 0.5f,
            onValueChange = {},
            iconRes = R.drawable.brightness,
            contentDescription = "Brightness",
            modifier = Modifier.width(150.dp),
        )
    }
}
