package com.lukeneedham.videodiary.ui.feature.exportdiary.create.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportDayThumbnail

@Composable
fun ExportDiaryThumbnailGrid(
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
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(70.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(Color.Black),
    ) {
        if (item.thumbnailFile != null) {
            AsyncImage(
                model = item.thumbnailFile,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.matchParentSize(),
            )
        }
    }
}
