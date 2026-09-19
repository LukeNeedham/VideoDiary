package com.lukeneedham.videodiary.ui.feature.recap.hub

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.SavedRecap
import com.lukeneedham.videodiary.ui.feature.common.DeleteConfirmDialog
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.theme.AppBackground
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography
import java.io.File
import java.time.LocalDate

@Composable
fun RecapHubPageContent(
    savedRecaps: List<SavedRecapWithThumbnails>,
    videoAspectRatio: Float?,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onCreateRecapClick: () -> Unit,
    onRecapClick: (SavedRecap) -> Unit,
    onDeleteClick: (String) -> Unit,
) {
    var pendingIdToDelete: String? by remember { mutableStateOf(null) }

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
                CreateRecapOptionRow(
                    title = "Create a recap",
                    description = "Make a recap of a month, week, year, or your own custom dates",
                    onClick = onCreateRecapClick,
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Saved recaps",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = Typography.Size.extraSmall,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }

            if (savedRecaps.isEmpty()) {
                item {
                    Text(
                        text = "No saved recaps yet. Open a recap above and save it to keep it here.",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = Typography.Size.extraSmall,
                    )
                }
            } else {
                items(savedRecaps, key = { it.recap.id }) { item ->
                    SavedRecapItem(
                        recap = item.recap,
                        dayVideoCount = item.dayVideoCount,
                        thumbnailFiles = item.thumbnailFiles,
                        videoAspectRatio = videoAspectRatio,
                        onClick = { onRecapClick(item.recap) },
                        onDeleteClick = { pendingIdToDelete = item.recap.id },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    val idToDelete = pendingIdToDelete
    if (idToDelete != null) {
        DeleteConfirmDialog(
            title = "Delete saved recap?",
            dismiss = {
                pendingIdToDelete = null
            },
            onConfirm = {
                onDeleteClick(idToDelete)
            },
        )
    }
}

@Composable
private fun CreateRecapOptionRow(
    title: String,
    description: String,
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
                text = title,
                color = Color.White,
                fontSize = Typography.Size.medium,
            )
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = Typography.Size.extraSmall,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewEmpty() {
    RecapHubPageContent(
        savedRecaps = emptyList(),
        videoAspectRatio = 9f / 16f,
        canGoBack = true,
        onBack = {},
        onCreateRecapClick = {},
        onRecapClick = {},
        onDeleteClick = {},
    )
}

@Preview
@Composable
private fun PreviewWithItems() {
    RecapHubPageContent(
        savedRecaps = listOf(
            SavedRecapWithThumbnails(
                recap = SavedRecap(
                    id = "1",
                    name = "Summer 2024",
                    startDate = LocalDate.of(2024, 6, 1),
                    endDate = LocalDate.of(2024, 8, 31),
                ),
                dayVideoCount = 45,
                thumbnailFiles = emptyList(),
            ),
            SavedRecapWithThumbnails(
                recap = SavedRecap(
                    id = "2",
                    name = "Holiday Trip",
                    startDate = LocalDate.of(2024, 12, 20),
                    endDate = LocalDate.of(2025, 1, 5),
                ),
                dayVideoCount = 12,
                thumbnailFiles = emptyList(),
            ),
        ),
        videoAspectRatio = 9f / 16f,
        canGoBack = true,
        onBack = {},
        onCreateRecapClick = {},
        onRecapClick = {},
        onDeleteClick = {},
    )
}
