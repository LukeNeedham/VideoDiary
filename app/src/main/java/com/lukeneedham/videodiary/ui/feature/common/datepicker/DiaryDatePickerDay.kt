package com.lukeneedham.videodiary.ui.feature.common.datepicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukeneedham.videodiary.domain.model.Day
import com.lukeneedham.videodiary.ui.theme.Typography
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DiaryDatePickerDay(
    day: Day,
    onClick: () -> Unit,
) {
    val date = day.date

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onClick()
            }
    ) {
        val hasVideo = day.video != null
        val alpha = if (hasVideo) 1f else 0.4f
        val color = Color.Black.copy(alpha = alpha)

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = date.dayOfMonth.toString(),
            color = color,
            fontSize = 26.sp,
        )
        Text(
            text = date.month.getDisplayName(
                TextStyle.SHORT,
                Locale.getDefault()
            ),
            color = color,
            fontSize = 13.sp,
        )
        Text(
            text = date.year.toString(),
            color = color,
            fontSize = 9.sp,
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview
@Composable
internal fun PreviewDiaryDatePickerDay() {
    DiaryDatePickerDay(
        day = MockDataDiaryDatePicker.day, onClick = {},
    )
}