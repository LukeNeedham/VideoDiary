package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton

@Composable
fun CalendarDaySelector(
    currentDate: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    openDayPicker: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        GlassIconButton(
            iconRes = R.drawable.chevron_left,
            contentDescription = "Previous",
            onClick = onPrevious,
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable {
                    openDayPicker()
                }
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = currentDate,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }

        GlassIconButton(
            iconRes = R.drawable.chevron_right,
            contentDescription = "Next",
            onClick = onNext,
        )
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
        modifier = Modifier.background(Color.Black)
    )
}
