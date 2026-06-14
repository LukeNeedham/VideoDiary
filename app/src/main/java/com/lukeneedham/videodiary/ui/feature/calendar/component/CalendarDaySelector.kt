package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassSurface

@Composable
fun CalendarDaySelector(
    currentDate: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    openDayPicker: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GlassSurface(
        modifier = modifier.height(IntrinsicSize.Min),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            @Composable
            fun Button(
                iconRes: Int,
                contentDescription: String,
                onClick: () -> Unit,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            onClick()
                        }
                ) {
                    Image(
                        painter = painterResource(iconRes),
                        contentDescription = contentDescription,
                        colorFilter = ColorFilter.tint(color = Color.White),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Button(
                iconRes = R.drawable.chevron_left,
                contentDescription = "Previous",
            ) {
                onPrevious()
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable {
                        openDayPicker()
                    }
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = currentDate,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }

            Button(
                iconRes = R.drawable.chevron_right,
                contentDescription = "Next",
            ) {
                onNext()
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarDaySelector() {
    CalendarDaySelector(
        currentDate = "2024-11-30",
        onPrevious = {},
        onNext = {},
        openDayPicker = {},
    )
}
