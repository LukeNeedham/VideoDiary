package com.lukeneedham.videodiary.ui.feature.exportdiary.hub

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.SavedExport
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography
import java.io.File

private val ThumbnailHeight = 60.dp
private val ThumbnailSpacing = 2.dp
private val EllipsisWidth = 24.dp
private val MinThumbnailWidth = 40.dp

@Composable
fun SavedExportItem(
    export: SavedExport,
    thumbnailFiles: List<File>,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AppSurfaceVariant)
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp),
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

        if (thumbnailFiles.isNotEmpty()) {
            SavedExportThumbnailRow(
                thumbnailFiles = thumbnailFiles,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun SavedExportThumbnailRow(
    thumbnailFiles: List<File>,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val maxFitCount = maxThumbnailCount(
            availableWidth = maxWidth,
            thumbnailCount = thumbnailFiles.size,
        )

        if (thumbnailFiles.size <= maxFitCount) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ThumbnailSpacing),
                modifier = Modifier.fillMaxWidth(),
            ) {
                for (file in thumbnailFiles) {
                    ThumbnailImage(
                        file = file,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        } else {
            val maxWithEllipsis = maxThumbnailCountWithEllipsis(maxWidth)
            val sideCount = maxWithEllipsis / 2
            val firstThumbnails = thumbnailFiles.take(sideCount)
            val lastThumbnails = thumbnailFiles.takeLast(sideCount)

            Row(
                horizontalArrangement = Arrangement.spacedBy(ThumbnailSpacing),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                for (file in firstThumbnails) {
                    ThumbnailImage(
                        file = file,
                        modifier = Modifier.weight(1f),
                    )
                }
                Text(
                    text = "…",
                    color = Color.White.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(EllipsisWidth),
                )
                for (file in lastThumbnails) {
                    ThumbnailImage(
                        file = file,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun ThumbnailImage(
    file: File,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = file,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .height(ThumbnailHeight),
    )
}

private fun maxThumbnailCount(
    availableWidth: Dp,
    thumbnailCount: Int,
): Int {
    if (thumbnailCount <= 1) return thumbnailCount
    val totalSpacing = ThumbnailSpacing * (thumbnailCount - 1)
    val widthPerThumbnail = (availableWidth - totalSpacing) / thumbnailCount
    if (widthPerThumbnail >= MinThumbnailWidth) return thumbnailCount
    return ((availableWidth + ThumbnailSpacing) / (MinThumbnailWidth + ThumbnailSpacing)).toInt()
        .coerceAtLeast(1)
}

private fun maxThumbnailCountWithEllipsis(availableWidth: Dp): Int {
    val widthForThumbnails = availableWidth - EllipsisWidth - ThumbnailSpacing * 2
    val count = ((widthForThumbnails + ThumbnailSpacing) / (MinThumbnailWidth + ThumbnailSpacing)).toInt()
    return (count / 2 * 2).coerceAtLeast(2)
}
