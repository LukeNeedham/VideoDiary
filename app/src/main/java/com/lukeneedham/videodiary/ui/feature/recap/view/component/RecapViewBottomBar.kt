package com.lukeneedham.videodiary.ui.feature.recap.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.toolbar.FlatIconButton
import com.lukeneedham.videodiary.ui.theme.Typography

// The minimum interactive size
private val ButtonSize = 48.dp

/**
 * A recap's static bottom bar: a borderless back button, the (wrapping) recap name, and
 * details/save/share icon buttons - plain, on the same black bar every other video toolbar uses
 * (no surface color/rounded corners of its own). All buttons are at least the minimum
 * interactive size, which leaves the name a little off-center.
 *
 * Clicking the name or the details button opens the recap's details sheet.
 */
@Composable
fun RecapViewBottomBar(
    name: String,
    isSaved: Boolean,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onInfoClick: () -> Unit,
    onToggleSavedClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.width(ButtonSize),
        ) {
            if (canGoBack) {
                FlatIconButton(
                    iconRes = R.drawable.back,
                    contentDescription = "Back",
                    onClick = onBack,
                    selected = true,
                    size = ButtonSize,
                )
            }
        }

        Text(
            text = name,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = Typography.Size.medium,
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onInfoClick)
                .padding(vertical = 4.dp),
        )

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.width(ButtonSize * 3),
        ) {
            FlatIconButton(
                iconRes = R.drawable.info,
                contentDescription = "Recap details",
                onClick = onInfoClick,
                selected = true,
                size = ButtonSize,
                iconSize = 20.dp,
            )
            FlatIconButton(
                iconRes = if (isSaved) R.drawable.heart_filled else R.drawable.heart_outline,
                contentDescription = if (isSaved) "Saved" else "Save",
                onClick = onToggleSavedClick,
                selected = true,
                size = ButtonSize,
                iconSize = 20.dp,
            )
            FlatIconButton(
                iconRes = R.drawable.share_android,
                contentDescription = "Share",
                onClick = onShareClick,
                selected = true,
                size = ButtonSize,
                iconSize = 20.dp,
            )
        }
    }
}
