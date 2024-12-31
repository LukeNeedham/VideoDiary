package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScroller(
    days: List<Day>,
    currentDayIndex: Int,
    videoAspectRatio: Float,
    onRecordTodayVideoClick: () -> Unit,
    onDeleteTodayVideoClick: () -> Unit,
    openDayPicker: () -> Unit,
    exportFullVideo: () -> Unit,
    setCurrentDayIndex: (Int) -> Unit,
    share: (ShareRequest) -> Unit,
) {
    val currentDay = days[currentDayIndex]

    /**
     * Video Player Controller, shared across all day videos.
     * This allows settings (like muted state) to persist across days.
     */
    val videoPlayerController = rememberVideoPlayerController()

    fun goToPage(index: Int) {
        if (index !in days.indices) return
        setCurrentDayIndex(index)
    }

    Column {
        // Toolbar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
                .padding(vertical = 5.dp, horizontal = 10.dp)
        ) {
            Text(
                text = "Video Diary",
                color = Color.White,
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.movie),
                contentDescription = "Export full video",
                colorFilter = ColorFilter.tint(color = Color.White),
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        exportFullVideo()
                    }
                    .padding(10.dp)
            )
        }

        val currentDateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val currentDateFormatted = currentDay.date.format(currentDateFormatter)

        Spacer(modifier = Modifier.height(5.dp))
        CalendarDaySelector(
            currentDate = currentDateFormatted,
            onPrevious = {
                goToPage(currentDayIndex - 1)
            },
            onNext = {
                goToPage(currentDayIndex + 1)
            },
            openDayPicker = openDayPicker,
        )
        Spacer(modifier = Modifier.height(10.dp))

        key(currentDay) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                CalendarDay(
                    day = currentDay,
                    videoAspectRatio = videoAspectRatio,
                    onRecordTodayVideoClick = onRecordTodayVideoClick,
                    onDeleteTodayVideoClick = onDeleteTodayVideoClick,
                    videoPlayerController = videoPlayerController,
                    share = share,
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview
@Composable
internal fun PreviewCalendarScroller() {
    CalendarScroller(
        days = MockDataCalendar.days,
        videoAspectRatio = 1f,
        onRecordTodayVideoClick = {},
        onDeleteTodayVideoClick = {},
        openDayPicker = {},
        setCurrentDayIndex = {},
        exportFullVideo = {},
        currentDayIndex = 0,
        share = {},
    )
}