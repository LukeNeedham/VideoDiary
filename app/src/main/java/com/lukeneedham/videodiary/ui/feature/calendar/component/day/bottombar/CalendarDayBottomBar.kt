package com.lukeneedham.videodiary.ui.feature.calendar.component.day.bottombar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.domain.model.ShareRequest
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.calendar.MockDataCalendar
import com.lukeneedham.videodiary.ui.feature.common.toolbar.FlatIconButton
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.VideoPlayerController
import com.lukeneedham.videodiary.ui.feature.common.videoplayer.rememberVideoPlayerController
import java.time.LocalDate

private val DayFontSizeLarge = 26.sp
private val DayFontSizeCompact = 20.sp
private val MonthFontSizeLarge = 14.sp
private val MonthFontSizeCompact = 11.sp
private val YearFontSize = 10.sp

@Composable
fun CalendarDayBottomBar(
    videoPlayerController: VideoPlayerController,
    day: Day,
    isEditable: Boolean,
    onMenuClick: () -> Unit,
    openDayPicker: () -> Unit,
    onRecordVideoClick: () -> Unit,
    onDeleteVideoClick: () -> Unit,
    share: (ShareRequest) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasVideo = day.videoFile != null
    val date = day.date
    val video = day.videoFile

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
    ) {
        FlatIconButton(
            iconRes = R.drawable.menu,
            contentDescription = "Menu",
            onClick = onMenuClick,
            selected = true,
            modifier = Modifier.align(Alignment.CenterStart),
        )

        CalendarDateDisplay(
            date = date,
            onClick = openDayPicker,
            modifier = Modifier.align(Alignment.Center),
        )

        if (hasVideo) {
            var isOverflowMenuOpen by remember { mutableStateOf(false) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                val muteButtonIcon =
                    if (videoPlayerController.isVolumeOn) R.drawable.volume_on else R.drawable.volume_off
                val muteButtonText = if (videoPlayerController.isVolumeOn) "Mute" else "Unmute"
                FlatIconButton(
                    iconRes = muteButtonIcon,
                    contentDescription = muteButtonText,
                    selected = true,
                    onClick = {
                        videoPlayerController.toggleVolumeOn()
                    },
                )

                Box {
                    FlatIconButton(
                        iconRes = R.drawable.more_vert,
                        contentDescription = "More options",
                        selected = true,
                        onClick = { isOverflowMenuOpen = true },
                    )

                    DropdownMenu(
                        expanded = isOverflowMenuOpen,
                        onDismissRequest = { isOverflowMenuOpen = false },
                    ) {
                        if (isEditable) {
                            OverflowMenuItem(
                                iconRes = R.drawable.retake,
                                text = "Retake",
                                onClick = {
                                    isOverflowMenuOpen = false
                                    onRecordVideoClick()
                                },
                            )
                        }

                        if (isEditable) {
                            OverflowMenuItem(
                                iconRes = R.drawable.delete,
                                text = "Delete",
                                onClick = {
                                    isOverflowMenuOpen = false
                                    onDeleteVideoClick()
                                },
                            )
                        }

                        OverflowMenuItem(
                            iconRes = R.drawable.share,
                            text = "Share",
                            onClick = {
                                isOverflowMenuOpen = false
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
                    }
                }
            }
        }
    }
}

@Composable
private fun OverflowMenuItem(
    iconRes: Int,
    text: String,
    onClick: () -> Unit,
) {
    DropdownMenuItem(onClick = onClick) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text)
    }
}

/**
 * Day number (big) over short month name (small) over year (small, shown only when it differs
 * from the current year). Showing the year shrinks the day/month text so the column keeps fitting
 * within the bar.
 */
@Composable
private fun CalendarDateDisplay(
    date: LocalDate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val showYear = date.year != LocalDate.now().year
    val dayFontSize = if (showYear) DayFontSizeCompact else DayFontSizeLarge
    val monthFontSize = if (showYear) MonthFontSizeCompact else MonthFontSizeLarge

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 2.dp),
    ) {
        Text(
            text = date.format(StandardDateTimeFormatter.dayOfMonth),
            color = Color.White,
            fontSize = dayFontSize,
            fontWeight = FontWeight.Bold,
            lineHeight = dayFontSize,
        )
        Text(
            text = date.format(StandardDateTimeFormatter.monthShort),
            color = Color.White.copy(alpha = 0.85f),
            fontSize = monthFontSize,
            lineHeight = monthFontSize,
        )
        if (showYear) {
            Text(
                text = date.format(StandardDateTimeFormatter.year),
                color = Color.White.copy(alpha = 0.65f),
                fontSize = YearFontSize,
                lineHeight = YearFontSize,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CalendarDayBottomBar(
        videoPlayerController = rememberVideoPlayerController(),
        day = MockDataCalendar.dayWithVideo,
        isEditable = false,
        onMenuClick = {},
        openDayPicker = {},
        onRecordVideoClick = {},
        onDeleteVideoClick = {},
        share = {},
    )
}

@Preview
@Composable
private fun PreviewEditableNoVideo() {
    CalendarDayBottomBar(
        videoPlayerController = rememberVideoPlayerController(),
        day = MockDataCalendar.dayWithoutVideo,
        isEditable = true,
        onMenuClick = {},
        openDayPicker = {},
        onRecordVideoClick = {},
        onDeleteVideoClick = {},
        share = {},
    )
}
