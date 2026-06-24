package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lukeneedham.videodiary.domain.model.Day
import java.io.File

@Composable
fun DiaryDatePickerDay(
    day: Day,
    videoAspectRatio: Float,
    onClick: () -> Unit,
) {
    val date = day.date

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(videoAspectRatio)
            .background(Color.Black)
            .clickable { onClick() }
    ) {
        DayThumbnail(thumbnailFile = day.thumbnailFile)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.35f))
                .padding(vertical = 1.dp)
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
            )
            val isFirstDayOfYear = date.dayOfYear == 1
            if (isFirstDayOfYear) {
                Text(
                    text = date.year.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 6.sp,
                )
            }
        }
    }
}

@Composable
private fun DayThumbnail(thumbnailFile: File?) {
    if (thumbnailFile == null) return

    AsyncImage(
        model = thumbnailFile,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize(),
    )
}

@Preview
@Composable
internal fun PreviewDiaryDatePickerDay() {
    DiaryDatePickerDay(
        day = MockDataDiaryDatePicker.day,
        videoAspectRatio = MockDataDiaryDatePicker.videoAspectRatio,
        onClick = {},
    )
}
