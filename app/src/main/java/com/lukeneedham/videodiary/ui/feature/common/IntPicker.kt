package com.lukeneedham.videodiary.ui.feature.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R

@Composable
fun IntPicker(
    value: Int,
    setValue: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(CircleShape)
            .border(width = 1.dp, color = Color.Gray)
    ) {
        @Composable
        fun Button(
            icon: Int,
            contentDesc: String,
            onClick: () -> Unit,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        color = Color.Gray,
                    )
                    .size(50.dp)
                    .clickable {
                        onClick()
                    }
            ) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = contentDesc,
                    colorFilter = ColorFilter.tint(color = Color.White)
                )
            }
        }

        Button(
            icon = R.drawable.minus,
            contentDesc = "Minus",
            onClick = {
                setValue(value - 1)
            }
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = value.toString(),
            color = Color.Black,
            modifier = Modifier.widthIn(min = 100.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Button(
            icon = R.drawable.add,
            contentDesc = "Add",
            onClick = {
                setValue(value + 1)
            }
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