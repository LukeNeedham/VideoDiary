package com.lukeneedham.videodiary.ui.feature.exportdiary.create.component

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukeneedham.videodiary.domain.util.date.StandardDateTimeFormatter
import com.lukeneedham.videodiary.ui.feature.exportdiary.create.model.ExportDayThumbnail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExportDiaryThumbnailGrid(
    thumbnails: List<ExportDayThumbnail>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        thumbnails.forEach { item ->
            ExportDiaryThumbnailItem(
                item = item,
                modifier = Modifier.width(70.dp),
            )
        }
    }
}

@Composable
private fun ExportDiaryThumbnailItem(
    item: ExportDayThumbnail,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f)
                .clip(RoundedCornerShape(4.dp))
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
        Text(
            text = item.date.format(StandardDateTimeFormatter.date),
            fontSize = 9.sp,
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}
