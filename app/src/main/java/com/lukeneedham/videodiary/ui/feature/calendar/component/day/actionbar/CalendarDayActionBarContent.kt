package com.lukeneedham.videodiary.ui.feature.calendar.component.day.actionbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.common.glass.GlassIconButton
import java.time.LocalDate

/** The content of the action bar, which gets splatted into a layout that the parent decides */
@Composable
fun CalendarDayActionBarContent(
    day: Day,
    allowRetakeForPastDays: Boolean,
    onRecordVideoClick: (date: LocalDate) -> Unit,
    onDeleteTodayVideoClick: () -> Unit,
    share: (ShareRequest) -> Unit,
) {
    val date = day.date
    val video = day.video
    val isToday = day.isToday

    @Composable
    fun ActionButton(
        iconRes: Int,
        contentDescription: String,
        onClick: () -> Unit,
    ) {
        GlassIconButton(
            iconRes = iconRes,
            contentDescription = contentDescription,
            onClick = onClick,
        )
    }

    if (video == null) {
        if (isToday || allowRetakeForPastDays) {
            ActionButton(
                iconRes = R.drawable.add,
                contentDescription = "Record",
                onClick = { onRecordVideoClick(date) },
            )
        }
    } else {
        if (isToday || allowRetakeForPastDays) {
            ActionButton(
                iconRes = R.drawable.retake,
                contentDescription = "Retake",
                onClick = { onRecordVideoClick(date) },
            )
        }

        ActionButton(
            iconRes = R.drawable.share,
            contentDescription = "Share",
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
                iconRes = R.drawable.delete,
                contentDescription = "Delete",
                onClick = onDeleteTodayVideoClick,
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayActionBar() {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        CalendarDayActionBarContent(
            day = MockDataCalendar.dayWithVideo,
            allowRetakeForPastDays = false,
            onRecordVideoClick = {},
            onDeleteTodayVideoClick = {},
            share = {},
        )
    }
}
