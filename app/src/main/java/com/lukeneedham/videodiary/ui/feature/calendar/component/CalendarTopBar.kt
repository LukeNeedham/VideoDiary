package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton

@Composable
fun CalendarTopBar(
    goToToday: () -> Unit,
    isToday: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        val todayAlpha = if (isToday) 0f else 1f
        GlassIconButton(
            iconRes = R.drawable.calendar_today,
            contentDescription = "Jump to today",
            onClick = goToToday,
            enabled = !isToday,
            modifier = Modifier.alpha(todayAlpha),
        )
    }
}

@Preview
@Composable
internal fun PreviewCalendarTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.DarkGray)
    ) {
        CalendarTopBar(
            goToToday = {},
            isToday = false,
        )
    }
}

@Preview
@Composable
internal fun PreviewCalendarTopBarToday() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.DarkGray)
    ) {
        CalendarTopBar(
            goToToday = {},
            isToday = true,
        )
    }
}
