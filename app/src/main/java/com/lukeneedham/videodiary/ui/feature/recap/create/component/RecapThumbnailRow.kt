package com.lukeneedham.videodiary.ui.feature.recap.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lukeneedham.videodiary.ui.feature.recap.model.RecapDayThumbnail
import com.lukeneedham.videodiary.ui.theme.AppTheme
import java.io.File
import java.time.LocalDate

@Composable
fun RecapThumbnailRow(
    thumbnails: List<RecapDayThumbnail>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier,
    ) {
        items(thumbnails, key = { it.date }) { item ->
            RecapThumbnailItem(item = item)
        }
    }
}

@Composable
private fun RecapThumbnailItem(
    item: RecapDayThumbnail,
    modifier: Modifier = Modifier,
) {
    if (item.thumbnailFile != null) {
        AsyncImage(
            model = item.thumbnailFile,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .height(96.dp)
                .aspectRatio(9f / 16f)
                .clip(RoundedCornerShape(8.dp)),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        RecapThumbnailRow(
            thumbnails = listOf(
                RecapDayThumbnail(date = LocalDate.of(2024, 6, 1), thumbnailFile = File("")),
                RecapDayThumbnail(date = LocalDate.of(2024, 6, 2), thumbnailFile = File("")),
            ),
        )
    }
}
