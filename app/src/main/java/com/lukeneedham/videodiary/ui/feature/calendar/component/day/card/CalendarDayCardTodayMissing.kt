package com.lukeneedham.videodiary.ui.feature.calendar.component.day.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
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
fun CalendarDayCardTodayMissing(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "No video for today yet!", color = Color.Black)
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .background(color = Color.Black, shape = CircleShape)
                    .padding(10.dp)
            ) {
                Image(
                    painterResource(R.drawable.ic_add),
                    contentDescription = "Record now!",
                    colorFilter = ColorFilter.tint(color = Color.White),
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewCalendarDayTodayEmpty() {
    CalendarDayCardTodayMissing()
}