package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            TopBarIconButton(
                iconRes = R.drawable.menu,
                contentDescription = "Menu",
                modifier = Modifier.clickable {
                    // todo - implement hamburger menu
                }
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
            TopBarIconButton(
                iconRes = R.drawable.calendar_today,
                contentDescription = "Jump to today",
                modifier = Modifier
                    .alpha(todayAlpha)
                    .clickable(
                        enabled = !isToday
                    ) {
                        goToToday()
                    }
            )
        }
    }
}

@Composable
private fun TopBarIconButton(
    iconRes: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .minimumInteractiveComponentSize()
            .clip(CircleShape)
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
