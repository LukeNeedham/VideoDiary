package com.lukeneedham.videodiary.ui.feature.storage

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.domain.model.VideoStorageLocation
import com.lukeneedham.videodiary.ui.feature.common.toolbar.SubpageToolbar
import com.lukeneedham.videodiary.ui.feature.common.toolbar.ToolbarPageLayout
import com.lukeneedham.videodiary.ui.theme.AccentAccept
import com.lukeneedham.videodiary.ui.theme.AccentHighlight
import com.lukeneedham.videodiary.ui.theme.AccentRecord
import com.lukeneedham.videodiary.ui.theme.AppBackground
import com.lukeneedham.videodiary.ui.theme.AppSurface
import com.lukeneedham.videodiary.ui.theme.Typography
import com.lukeneedham.videodiary.ui.theme.AppTheme

@Composable
internal fun VideoStoragePageContent(
    location: VideoStorageLocation,
    isRemovableStorageAvailable: Boolean,
    isChangingLocation: Boolean,
    error: String?,
    onLocationSelected: (VideoStorageLocation) -> Unit,
    canGoBack: Boolean,
    onBack: () -> Unit,
) {
    ToolbarPageLayout(
        bottomBar = { SubpageToolbar(canGoBack = canGoBack, onBack = onBack) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            Text(
                text = "Video storage",
                color = Color.White,
                fontSize = Typography.Size.big,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Changing the location moves your existing diary videos",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = Typography.Size.extraSmall,
                modifier = Modifier.padding(top = 4.dp),
            )

            Section(title = "Location") {
                StorageOptionRow(
                    iconRes = R.drawable.movie,
                    accent = AccentAccept,
                    title = "Internal storage",
                    description = "Keep diary videos in the app's internal storage",
                    selected = location == VideoStorageLocation.Internal,
                    enabled = !isChangingLocation,
                    onClick = { onLocationSelected(VideoStorageLocation.Internal) },
                )
                Divider()
                StorageOptionRow(
                    iconRes = R.drawable.movie,
                    accent = AccentAccept,
                    title = "SD card",
                    description = if (isRemovableStorageAvailable) {
                        "Keep diary videos in this app's folder on the SD card. " +
                            "They are deleted if the app is uninstalled"
                    } else {
                        "Insert a writable SD card to use this location"
                    },
                    selected = location == VideoStorageLocation.RemovableStorage,
                    enabled = isRemovableStorageAvailable && !isChangingLocation,
                    onClick = { onLocationSelected(VideoStorageLocation.RemovableStorage) },
                )
            }

            if (isChangingLocation) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 24.dp, start = 4.dp),
                ) {
                    CircularProgressIndicator(
                        color = AccentHighlight,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Moving videos…",
                        color = Color.White,
                        fontSize = Typography.Size.medium,
                    )
                }
            }
            if (error != null) {
                Notice(accent = AccentRecord, text = error)
            }
        }
    }
}

/** A titled group of rows, shown together in one rounded card. */
@Composable
private fun Section(
    title: String,
    content: @Composable () -> Unit,
) {
    Text(
        text = title.uppercase(),
        color = Color.White.copy(alpha = 0.5f),
        fontSize = Typography.Size.extraSmall,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(top = 28.dp, bottom = 8.dp, start = 4.dp),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppSurface)
    ) {
        content()
    }
}

@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 72.dp)
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.08f))
    )
}

@Composable
private fun StorageOptionRow(
    iconRes: Int,
    accent: Color,
    title: String,
    description: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val contentAlpha = if (enabled) 1f else 0.4f
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(accent.copy(alpha = 0.15f * contentAlpha))
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(accent.copy(alpha = contentAlpha)),
                modifier = Modifier.size(22.dp),
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White.copy(alpha = contentAlpha),
                fontSize = Typography.Size.medium,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.6f * contentAlpha),
                fontSize = Typography.Size.extraSmall,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        RadioButton(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            colors = RadioButtonDefaults.colors(
                selectedColor = AccentAccept,
                unselectedColor = Color.White.copy(alpha = 0.4f),
                disabledColor = Color.White.copy(alpha = 0.2f),
            ),
        )
    }
}

/** A message in a rounded card, tinted with [accent] like the icon boxes. */
@Composable
private fun Notice(
    accent: Color,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(accent.copy(alpha = 0.15f))
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.info),
            contentDescription = null,
            colorFilter = ColorFilter.tint(accent),
            modifier = Modifier.size(22.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = Typography.Size.extraSmall,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        VideoStoragePageContent(
            location = VideoStorageLocation.Internal,
            isRemovableStorageAvailable = true,
            isChangingLocation = false,
            error = null,
            onLocationSelected = {},
            canGoBack = true,
            onBack = {},
        )
    }
}

@Preview
@Composable
private fun PreviewVideoStoragePageContentUnavailableAndError() {
    AppTheme {
        VideoStoragePageContent(
            location = VideoStorageLocation.RemovableStorage,
            isRemovableStorageAvailable = false,
            isChangingLocation = false,
            error = "Could not move videos. Your current storage location has not changed.",
            onLocationSelected = {},
            canGoBack = true,
            onBack = {},
        )
    }
}
