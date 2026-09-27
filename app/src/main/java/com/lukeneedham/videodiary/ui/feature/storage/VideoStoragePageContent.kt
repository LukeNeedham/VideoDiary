package com.lukeneedham.videodiary.ui.feature.storage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.domain.model.VideoStorageLocation
import com.lukeneedham.videodiary.ui.feature.common.toolbar.GenericToolbar
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun VideoStoragePageContent(
    location: VideoStorageLocation,
    isRemovableStorageAvailable: Boolean,
    isChangingLocation: Boolean,
    error: String?,
    onLocationSelected: (VideoStorageLocation) -> Unit,
    onSelectCustomFolder: () -> Unit,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        GenericToolbar(canGoBack = canGoBack, onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = "Video storage", color = Color.Black, fontSize = Typography.Size.big)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Changing the location moves existing diary videos. Videos stored on removable storage are deleted if the app is uninstalled.",
                color = Color.Black,
                fontSize = Typography.Size.extraSmall,
            )
            Spacer(modifier = Modifier.height(16.dp))
            StorageOption(
                title = "Internal storage",
                description = "Store diary videos in the app's internal storage.",
                selected = location == VideoStorageLocation.Internal,
                enabled = !isChangingLocation,
                onClick = { onLocationSelected(VideoStorageLocation.Internal) },
            )
            StorageOption(
                title = "SD card",
                description = if (isRemovableStorageAvailable) {
                    "Store diary videos in this app's folder on the SD card."
                } else {
                    "Insert a writable SD card to use this location."
                },
                selected = location == VideoStorageLocation.RemovableStorage,
                enabled = isRemovableStorageAvailable && !isChangingLocation,
                onClick = { onLocationSelected(VideoStorageLocation.RemovableStorage) },
            )
            val isCustomSelected = location is VideoStorageLocation.Custom
            val customDescription = if (location is VideoStorageLocation.Custom) {
                "Current folder: ${location.displayName ?: location.uriString}"
            } else {
                "Select a custom folder on your device to store diary videos."
            }
            StorageOption(
                title = "Custom folder",
                description = customDescription,
                selected = isCustomSelected,
                enabled = !isChangingLocation,
                onClick = onSelectCustomFolder,
            )
            if (isChangingLocation) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.height(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Moving videos…", color = Color.Black)
                }
            }
            if (error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = error, color = Color.Red, fontSize = Typography.Size.extraSmall)
            }
        }
    }
}

@Composable
private fun StorageOption(
    title: String,
    description: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 10.dp),
    ) {
        RadioButton(selected = selected, onClick = onClick, enabled = enabled)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, color = if (enabled) Color.Black else Color.Gray, fontSize = Typography.Size.small)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, color = if (enabled) Color.Black else Color.Gray, fontSize = Typography.Size.extraSmall)
        }
    }
}

@Preview
@Composable
private fun PreviewVideoStoragePageContent() {
    VideoStoragePageContent(
        location = VideoStorageLocation.Internal,
        isRemovableStorageAvailable = true,
        isChangingLocation = false,
        error = null,
        onLocationSelected = {},
        onSelectCustomFolder = {},
        canGoBack = true,
        onBack = {},
    )
}
