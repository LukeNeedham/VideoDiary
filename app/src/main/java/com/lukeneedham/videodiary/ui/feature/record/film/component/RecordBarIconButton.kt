package com.lukeneedham.videodiary.ui.feature.record.film.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R

/**
 * A plain icon button for the record video page's bottom bar - just the icon, with no glass
 * background/border, matching the bar's flat look. [selected] dims the icon down when off, so an
 * active control (e.g. an open slider popup) reads as brighter/highlighted than the rest.
 */
@Composable
fun RecordBarIconButton(
    iconRes: Int,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    size: Dp = 50.dp,
    iconSize: Dp = 24.dp,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clickable(onClick = onClick),
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(Color.White.copy(alpha = if (selected) 1f else 0.6f)),
            modifier = Modifier.size(iconSize),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Row(
        modifier = Modifier.background(Color.Black),
    ) {
        RecordBarIconButton(
            iconRes = R.drawable.close,
            contentDescription = "Close",
            onClick = {},
        )
        RecordBarIconButton(
            iconRes = R.drawable.brightness,
            contentDescription = "Brightness",
            selected = true,
            onClick = {},
        )
    }
}
