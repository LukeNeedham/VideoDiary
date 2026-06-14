package com.lukeneedham.videodiary.ui.feature.calendar.component.day.actionbar

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.common.Button
import java.time.LocalDate

/** The content of the action bar, which gets splatted into a layout that the parent decides */
@Composable
fun CalendarDayActionBarContent(
    day: Day,
    allowRetakeForPastDays: Boolean,
    onRecordVideoClick: (date: LocalDate) -> Unit,
    onDeleteTodayVideoClick: () -> Unit,
    share: (ShareRequest) -> Unit,
    buttonModifier: Modifier,
) {
    val date = day.date
    val video = day.video
    val isToday = day.isToday

    @Composable
    fun ActionButton(
        text: String,
        onClick: () -> Unit,
    ) {
        Button(
            text = text,
            onClick = onClick,
            modifier = buttonModifier
        )
    }

    if (video == null) {
        if (isToday) {
            ActionButton(
                text = "Record",
                onClick = { onRecordVideoClick(date) },
            )
        }
    } else {
        if (isToday || allowRetakeForPastDays) {
            ActionButton(
                text = "Retake",
                onClick = { onRecordVideoClick(date) },
            )
        }

        ActionButton(
            text = "Share",
            onClick = {
                val dateText = date.format(StandardDateTimeFormatter.date)
                val text = "Video Diary: $dateText"
                val request = ShareRequest(
                    title = text,
                    text = text,
                    video = video,
                )
                share(request)
            },
        )

        if (isToday) {
            ActionButton(
                text = "Delete",
                onClick = onDeleteTodayVideoClick,
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayActionBar() {
    Row {
        CalendarDayActionBarContent(
            day = MockDataCalendar.dayWithVideo,
            allowRetakeForPastDays = false,
            onRecordVideoClick = {},
            onDeleteTodayVideoClick = {},
            share = {},
            buttonModifier = Modifier.weight(1f),
        )
    }
}