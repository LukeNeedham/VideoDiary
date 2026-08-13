package com.lukeneedham.videodiary.ui.feature.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.theme.AccentHighlight

@Composable
fun IntPicker(
    value: Int,
    setValue: (Int) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = AccentHighlight,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier,
    ) {
        IntPickerButton(
            iconRes = R.drawable.minus,
            contentDescription = "Decrease",
            accentColor = accentColor,
            onClick = { setValue(value - 1) },
        )
        Text(
            text = value.toString(),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(min = 64.dp),
        )
        IntPickerButton(
            iconRes = R.drawable.add,
            contentDescription = "Increase",
            accentColor = accentColor,
            onClick = { setValue(value + 1) },
        )
    }
}

@Composable
private fun IntPickerButton(
    iconRes: Int,
    contentDescription: String,
    accentColor: Color,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(52.dp)
            .shadow(elevation = 3.dp, shape = CircleShape)
            .background(color = accentColor, shape = CircleShape)
            .clickable(onClick = onClick),
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(color = Color.White),
            modifier = Modifier.size(22.dp),
        )
    }
}

@Preview
@Composable
internal fun PreviewIntPicker() {
    IntPicker(
        value = 3,
        setValue = {},
    )
}
