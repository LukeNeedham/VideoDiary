package com.lukeneedham.videodiary.ui.feature.recap.create.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.recap.create.MockDataRecapCreateCustom
import com.lukeneedham.videodiary.ui.theme.AccentHighlight
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.GlassBorder
import com.lukeneedham.videodiary.ui.theme.Typography
import java.time.LocalDate

@Composable
fun RecapDatePicker(
    label: String,
    date: LocalDate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(AppSurfaceVariant)
            .border(width = 1.dp, color = GlassBorder, shape = shape)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = label.uppercase(),
            color = AccentHighlight,
            fontSize = Typography.Size.extraSmall,
        )
        Text(
            text = date.format(StandardDateTimeFormatter.dateLong),
            color = Color.White,
            fontSize = Typography.Size.small,
        )
    }
}

@Preview
@Composable
internal fun PreviewRecapDatePicker() {
    RecapDatePicker(
        label = "From",
        date = MockDataRecapCreateCustom.startDate,
        onClick = {},
    )
}
