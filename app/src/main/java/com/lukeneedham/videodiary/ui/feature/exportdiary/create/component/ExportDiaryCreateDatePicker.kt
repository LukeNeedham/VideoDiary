package com.lukeneedham.videodiary.ui.feature.exportdiary.create.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.MockDataExportDiaryCreate
import com.lukeneedham.videodiary.ui.theme.Typography
import java.time.LocalDate

@Composable
fun ExportDiaryCreateDatePicker(
    date: LocalDate,
    label: String,
    onChange: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "$label:",
                fontSize = Typography.Size.big,
            )
            Spacer(modifier = Modifier.height(2.dp))
            val dateText = date.format(StandardDateTimeFormatter.date)
            Text(
                text = dateText,
                fontSize = Typography.Size.small,
            )
            Text(
                text = "(inclusive)",
                fontSize = Typography.Size.extraSmall,
            )
        }

        Button(
            text = "Change",
            onClick = {
                onChange()
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
internal fun PreviewExportDiaryDatePicker() {
    ExportDiaryCreateDatePicker(
        date = MockDataExportDiaryCreate.startDate,
        label = "From",
        onChange = {},
    )
}