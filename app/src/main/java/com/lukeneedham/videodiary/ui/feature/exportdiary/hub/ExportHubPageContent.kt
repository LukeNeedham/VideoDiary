package com.lukeneedham.videodiary.ui.feature.exportdiary.hub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.SavedExport
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.theme.AppBackground
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography
import java.io.File
import java.time.LocalDate

@Composable
fun ExportHubPageContent(
    savedExports: List<SavedExport>,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onCreateExportClick: () -> Unit,
    onExportClick: (SavedExport) -> Unit,
    onDeleteClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        GenericToolbar(
            canGoBack = canGoBack,
            onBack = onBack,
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            item {
                CreateExportButton(onClick = onCreateExportClick)
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (savedExports.isNotEmpty()) {
                item {
                    Text(
                        text = "Saved exports",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = Typography.Size.extraSmall,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }

                items(savedExports, key = { it.id }) { export ->
                    SavedExportItem(
                        export = export,
                        onClick = { onExportClick(export) },
                        onDeleteClick = { onDeleteClick(export.id) },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun CreateExportButton(
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AppSurfaceVariant)
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.add),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(28.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Create new export",
                color = Color.White,
                fontSize = Typography.Size.medium,
            )
            Text(
                text = "Combine diary videos into a single recap video",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = Typography.Size.extraSmall,
            )
        }
    }
}

@Composable
private fun SavedExportItem(
    export: SavedExport,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AppSurfaceVariant)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = export.name,
                color = Color.White,
                fontSize = Typography.Size.medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            val start = export.startDate.format(StandardDateTimeFormatter.date)
            val end = export.endDate.format(StandardDateTimeFormatter.date)
            Text(
                text = "$start to $end · ${export.dayVideoCount} videos",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = Typography.Size.extraSmall,
            )
        }

        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.size(40.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.delete),
                contentDescription = "Delete",
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewEmpty() {
    ExportHubPageContent(
        savedExports = emptyList(),
        canGoBack = true,
        onBack = {},
        onCreateExportClick = {},
        onExportClick = {},
        onDeleteClick = {},
    )
}

@Preview
@Composable
private fun PreviewWithItems() {
    ExportHubPageContent(
        savedExports = listOf(
            SavedExport(
                id = "1",
                name = "Summer 2024",
                videoFile = File("mock"),
                startDate = LocalDate.of(2024, 6, 1),
                endDate = LocalDate.of(2024, 8, 31),
                dayVideoCount = 45,
            ),
            SavedExport(
                id = "2",
                name = "Holiday Trip",
                videoFile = File("mock"),
                startDate = LocalDate.of(2024, 12, 20),
                endDate = LocalDate.of(2025, 1, 5),
                dayVideoCount = 12,
            ),
        ),
        canGoBack = true,
        onBack = {},
        onCreateExportClick = {},
        onExportClick = {},
        onDeleteClick = {},
    )
}
