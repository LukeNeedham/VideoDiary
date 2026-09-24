package com.lukeneedham.videodiary.ui.feature.recap.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.toolbar.FlatIconButton
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography

/**
 * A recap's static bottom bar: a borderless back button, the (wrapping, truly-centered) recap
 * name, and save/share icon buttons.
 */
@Composable
fun RecapViewBottomBar(
    name: String,
    isSaved: Boolean,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onToggleSavedClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(AppSurfaceVariant)
            .padding(horizontal = 14.dp, vertical = 14.dp),
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.width(96.dp),
        ) {
            if (canGoBack) {
                FlatIconButton(
                    iconRes = R.drawable.back,
                    contentDescription = "Back",
                    onClick = onBack,
                    selected = true,
                    size = 44.dp,
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
                .padding(vertical = 4.dp),
        )

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.width(96.dp),
        ) {
            FlatIconButton(
                iconRes = if (isSaved) R.drawable.heart_filled else R.drawable.heart_outline,
                contentDescription = if (isSaved) "Saved" else "Save",
                onClick = onToggleSavedClick,
                selected = true,
                size = 44.dp,
                iconSize = 20.dp,
            )
            FlatIconButton(
                iconRes = R.drawable.share_android,
                contentDescription = "Share",
                onClick = onShareClick,
                selected = true,
                size = 44.dp,
                iconSize = 20.dp,
            )
        }
    }
}
