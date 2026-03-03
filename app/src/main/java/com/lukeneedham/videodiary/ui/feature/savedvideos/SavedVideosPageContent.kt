package com.lukeneedham.videodiary.ui.feature.savedvideos

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.SavedExport
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun SavedVideosPageContent(
    savedVideos: List<SavedExport>,
    onBack: () -> Unit,
    onClick: (SavedExport) -> Unit,
    onDelete: (SavedExport) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        GenericToolbar(
            canGoBack = true,
            onBack = onBack,
        )

        if (savedVideos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No saved videos yet",
                    color = Color.Black,
                    fontSize = Typography.Size.small,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(savedVideos, key = { it.id }) { video ->
                    SavedVideoItem(
                        video = video,
                        onClick = { onClick(video) },
                        onDelete = { onDelete(video) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedVideoItem(
    video: SavedExport,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = video.name,
                color = Color.Black,
                fontSize = Typography.Size.small,
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable { onDelete() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                // todo: should be delete icon
                painter = painterResource(R.drawable.play),
                contentDescription = "Delete",
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
