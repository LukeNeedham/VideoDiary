package com.lukeneedham.videodiary.ui.feature.exportdiary.create.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.MockDataExportDiaryCreate
import com.lukeneedham.videodiary.ui.theme.Typography
import java.time.LocalDate

@Composable
fun ExportDiaryCreateDatePicker(
    date: LocalDate,
    onClick: () -> Unit,
) {
    val dateText = date.format(StandardDateTimeFormatter.date)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable { onClick() }
    ) {
        Text(
            text = dateText,
            color = Color.White,
            fontSize = Typography.Size.small,
        )
    }
}

@Preview
@Composable
internal fun PreviewExportDiaryDatePicker() {
    ExportDiaryCreateDatePicker(
        date = MockDataExportDiaryCreate.startDate,
        onClick = {},
    )
}
