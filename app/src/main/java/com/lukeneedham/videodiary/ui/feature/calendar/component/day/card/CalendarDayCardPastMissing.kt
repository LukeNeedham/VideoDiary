package com.lukeneedham.videodiary.ui.feature.calendar.component.day.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CalendarDayCardPastMissing() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "No video for this day! You missed it!", color = Color.Black)
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayMissing() {
    CalendarDayCardPastMissing()
}