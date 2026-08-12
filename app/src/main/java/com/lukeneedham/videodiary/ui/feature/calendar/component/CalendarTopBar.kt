package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton

@Composable
fun CalendarTopBar(
    currentDateFormatted: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    openDayPicker: () -> Unit,
    goToToday: () -> Unit,
    onMenuClick: () -> Unit,
    isToday: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        GlassIconButton(
            iconRes = R.drawable.menu,
            contentDescription = "Menu",
            onClick = onMenuClick,
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(1f)
        ) {
            CalendarDaySelector(
                currentDate = currentDateFormatted,
                onPrevious = onPrevious,
                onNext = onNext,
                openDayPicker = openDayPicker,
            )
        }

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
            currentDateFormatted = "30 Nov",
            onPrevious = {},
            onNext = {},
            openDayPicker = {},
            goToToday = {},
            onMenuClick = {},
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
            currentDateFormatted = "30 Nov",
            onPrevious = {},
            onNext = {},
            openDayPicker = {},
            goToToday = {},
            onMenuClick = {},
            isToday = true,
        )
    }
}
