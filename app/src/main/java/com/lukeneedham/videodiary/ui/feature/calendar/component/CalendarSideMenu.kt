package com.lukeneedham.videodiary.ui.feature.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.BuildConfig
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.theme.AppSurface

@Composable
fun CalendarSideMenu(
    onExportClick: () -> Unit,
    onDebugClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(AppSurface)
            .padding(vertical = 24.dp)
    ) {
        CalendarSideMenuItem(
            iconRes = R.drawable.movie,
            text = "Export diary",
            onClick = onExportClick,
        )

        if (BuildConfig.DEBUG) {
            CalendarSideMenuItem(
                iconRes = R.drawable.bug,
                text = "Debug",
                onClick = onDebugClick,
            )
        }
    }
}

@Composable
private fun CalendarSideMenuItem(
    iconRes: Int,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = text,
            color = Color.White,
        )
    }
}

@Preview
@Composable
private fun PreviewCalendarSideMenu() {
    CalendarSideMenu(
        onExportClick = {},
        onDebugClick = {},
        modifier = Modifier.width(280.dp),
    )
}
