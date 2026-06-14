package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R

@Composable
fun CalendarTopBar(
    currentDateFormatted: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    openDayPicker: () -> Unit,
    goToToday: () -> Unit,
    isToday: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .safeDrawingPadding()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        TopBarIconButton(
            iconRes = R.drawable.menu,
            contentDescription = "Menu",
            onClick = {},
            modifier = Modifier.align(Alignment.CenterStart),
        )

        CalendarDaySelector(
            currentDate = currentDateFormatted,
            onPrevious = onPrevious,
            onNext = onNext,
            openDayPicker = openDayPicker,
            modifier = Modifier.align(Alignment.Center),
        )

        if (!isToday) {
            TopBarIconButton(
                iconRes = R.drawable.calendar_today,
                contentDescription = "Jump to today",
                onClick = goToToday,
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }
    }
}

@Composable
private fun TopBarIconButton(
    iconRes: Int,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(48.dp)
            .clickable(onClick = onClick),
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(24.dp),
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
            .background(Color.Black)
    ) {
        CalendarTopBar(
            currentDateFormatted = "30 Nov",
            onPrevious = {},
            onNext = {},
            openDayPicker = {},
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
            .background(Color.Black)
    ) {
        CalendarTopBar(
            currentDateFormatted = "30 Nov",
            onPrevious = {},
            onNext = {},
            openDayPicker = {},
            goToToday = {},
            isToday = true,
        )
    }
}
