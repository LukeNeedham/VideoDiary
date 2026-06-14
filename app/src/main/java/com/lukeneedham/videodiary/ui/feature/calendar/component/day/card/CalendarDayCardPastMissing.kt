package com.lukeneedham.videodiary.ui.feature.calendar.component.day.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CalendarDayCardPastMissing(
    onRecordVideoClick: (() -> Unit)? = null,
) {
    val modifier = if (onRecordVideoClick != null) {
        Modifier.clickable(onClick = onRecordVideoClick)
    } else {
        Modifier
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        val text = if (onRecordVideoClick != null) {
            "No video for this day! Tap to record one now."
        } else {
            "No video for this day! You missed it!"
        }
        Text(text = text, color = Color.White)
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayMissing() {
    CalendarDayCardPastMissing()
}

@Preview
@Composable
internal fun PreviewCalendarDayMissingRecordable() {
    CalendarDayCardPastMissing(onRecordVideoClick = {})
}
