package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CalendarDaySelector(
    currentDate: String,
    openDayPicker: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
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
}

@Preview
@Composable
internal fun PreviewCalendarDaySelector() {
    CalendarDaySelector(
        currentDate = "2024-11-30",
        openDayPicker = {},
        modifier = Modifier.background(Color.Black)
    )
}
