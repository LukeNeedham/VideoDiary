package com.lukeneedham.videodiary.ui.feature.exportdiary.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportDayThumbnail

@Composable
fun ExportDiaryThumbnailRow(
    thumbnails: List<ExportDayThumbnail>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier,
    ) {
        items(thumbnails, key = { it.date }) { item ->
            ExportDiaryThumbnailItem(item = item)
        }
    }
}

@Composable
private fun ExportDiaryThumbnailItem(
    item: ExportDayThumbnail,
    modifier: Modifier = Modifier,
) {
    if (item.thumbnailFile != null) {
        AsyncImage(
            model = item.thumbnailFile,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier
                .height(75.dp),
        )
    }
}
