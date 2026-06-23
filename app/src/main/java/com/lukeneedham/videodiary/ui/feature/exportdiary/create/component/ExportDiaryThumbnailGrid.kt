package com.lukeneedham.videodiary.ui.feature.exportdiary.create.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportDayThumbnail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ExportDiaryThumbnailGrid(
    thumbnails: List<ExportDayThumbnail>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
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
            .height(48.dp)
            .aspectRatio(9f / 16f)
            .clip(RoundedCornerShape(3.dp))
            .background(Color.Black),
    ) {
        val thumbnailFile = item.thumbnailFile
        if (thumbnailFile != null) {
            val bitmap by produceState(
                initialValue = null as android.graphics.Bitmap?,
                thumbnailFile,
            ) {
                value = withContext(Dispatchers.IO) {
                    BitmapFactory.decodeFile(thumbnailFile.absolutePath)
                }
            }
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize(),
                )
            }
        }
    }
}
