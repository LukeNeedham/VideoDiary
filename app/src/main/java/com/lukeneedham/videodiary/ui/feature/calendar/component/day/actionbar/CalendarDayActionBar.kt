package com.lukeneedham.videodiary.ui.feature.calendar.component.day.actionbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.common.Button
import java.time.format.DateTimeFormatter

@Composable
fun CalendarDayActionBar(
    day: Day,
    onRecordTodayVideoClick: () -> Unit,
    onDeleteTodayVideoClick: () -> Unit,
    share: (ShareRequest) -> Unit
) {
    val date = day.date
    val video = day.video
    val isToday = day.isToday

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            // Fixed height, so bar will always render regardless of whether any buttons render
            .height(60.dp)
            .fillMaxWidth()
    ) {
        @Composable
        fun ActionButton(
            text: String,
            onClick: () -> Unit,
        ) {
            Button(
                text = text,
                onClick = onClick,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )
        }

        if (video == null) {
            if (isToday) {
                ActionButton(
                    text = "Record",
                    onClick = onRecordTodayVideoClick,
                )
            }
        } else {
            if (isToday) {
                ActionButton(
                    text = "Retake",
                    onClick = onRecordTodayVideoClick,
                )

                ActionButton(
                    text = "Delete",
                    onClick = onDeleteTodayVideoClick,
                )
            }

            ActionButton(
                text = "Share",
                onClick = {
                    val dateText = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    val text = "Video Diary: $dateText"
                    val request = ShareRequest(
                        title = text,
                        text = text,
                        video = video,
                    )
                    share(request)
                },
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayActionBar() {
    CalendarDayActionBar(
        day = MockDataCalendar.day,
        onRecordTodayVideoClick = {},
        onDeleteTodayVideoClick = {},
        share = {},
    )
}