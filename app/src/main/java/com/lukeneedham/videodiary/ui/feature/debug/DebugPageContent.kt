package com.lukeneedham.videodiary.ui.feature.debug

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
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
import com.lukeneedham.videodiary.ui.feature.common.toolbar.SubpageToolbar
import com.lukeneedham.videodiary.ui.feature.common.toolbar.ToolbarPageLayout
import com.lukeneedham.videodiary.ui.theme.AccentAccept
import com.lukeneedham.videodiary.ui.theme.AccentHighlight
import com.lukeneedham.videodiary.ui.theme.AppBackground
import com.lukeneedham.videodiary.ui.theme.AppSurface
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography

@Composable
fun DebugPageContent(
    onFillWithMockDataClick: () -> Unit,
    allowRetakeForPastDays: Boolean,
    onAllowRetakeForPastDaysChange: (Boolean) -> Unit,
    onResyncThumbnailsClick: () -> Unit,
    onCrashLogClick: () -> Unit,
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
                text = "Debug",
                color = Color.White,
                fontSize = Typography.Size.big,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Developer tools, only available in debug builds",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = Typography.Size.extraSmall,
                modifier = Modifier.padding(top = 4.dp),
            )

            Section(title = "Data") {
                DebugActionRow(
                    iconRes = R.drawable.movie,
                    accent = AccentAccept,
                    title = "Fill with mock data",
                    description = "Overwrites all diary data with a random mix of mock videos, " +
                        "spanning the last 5 weeks",
                    onClick = onFillWithMockDataClick,
                )
                Divider()
                DebugActionRow(
                    iconRes = R.drawable.camera,
                    accent = AccentAccept,
                    title = "Resync thumbnails",
                    description = "Deletes all generated thumbnails and recalculates them " +
                        "from the saved videos",
                    onClick = onResyncThumbnailsClick,
                )
            }

            Section(title = "Behaviour") {
                DebugSwitchRow(
                    iconRes = R.drawable.retake,
                    accent = AccentHighlight,
                    title = "Allow retake for past days",
                    description = "Shows the Retake button for past days, allowing the video " +
                        "for that day to be re-recorded. Normally only today's video can be retaken",
                    checked = allowRetakeForPastDays,
                    onCheckedChange = onAllowRetakeForPastDaysChange,
                )
            }

            Section(title = "Diagnostics") {
                DebugActionRow(
                    iconRes = R.drawable.bug,
                    accent = AccentHighlight,
                    title = "Crash logs",
                    description = "View all fatal crash logs recorded by the app",
                    onClick = onCrashLogClick,
                    showChevron = true,
                )
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
private fun DebugActionRow(
    iconRes: Int,
    accent: Color,
    title: String,
    description: String,
    onClick: () -> Unit,
    showChevron: Boolean = false,
) {
    DebugRow(
        iconRes = iconRes,
        accent = accent,
        title = title,
        description = description,
        onClick = onClick,
    ) {
        if (showChevron) {
            Image(
                painter = painterResource(R.drawable.chevron_right),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.4f)),
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun DebugSwitchRow(
    iconRes: Int,
    accent: Color,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    DebugRow(
        iconRes = iconRes,
        accent = accent,
        title = title,
        description = description,
        onClick = { onCheckedChange(!checked) },
    ) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AccentAccept,
                checkedTrackAlpha = 1f,
                uncheckedThumbColor = Color.White.copy(alpha = 0.8f),
                uncheckedTrackColor = AppSurfaceVariant,
                uncheckedTrackAlpha = 1f,
            ),
        )
    }
}

@Composable
private fun DebugRow(
    iconRes: Int,
    accent: Color,
    title: String,
    description: String,
    onClick: () -> Unit,
    trailing: @Composable RowScope.() -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(accent.copy(alpha = 0.15f))
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(accent),
                modifier = Modifier.size(22.dp),
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = Typography.Size.medium,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.6f),
                fontSize = Typography.Size.extraSmall,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        trailing()
    }
}

@Preview
@Composable
internal fun PreviewDebugPageContent() {
    DebugPageContent(
        onFillWithMockDataClick = {},
        allowRetakeForPastDays = true,
        onAllowRetakeForPastDaysChange = {},
        onResyncThumbnailsClick = {},
        onCrashLogClick = {},
        canGoBack = true,
        onBack = {},
    )
}
