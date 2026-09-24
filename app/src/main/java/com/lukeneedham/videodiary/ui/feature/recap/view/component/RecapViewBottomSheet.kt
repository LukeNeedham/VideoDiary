package com.lukeneedham.videodiary.ui.feature.recap.view.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.R
import com.lukeneedham.videodiary.ui.feature.common.toolbar.FlatIconButton
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography

/**
 * The bottom sheet for viewing a recap: a compact header (back / name / save / share) that's
 * always visible, plus a chevron that expands the sheet to reveal export options (currently just
 * the date-stamp toggle) below the header - the header never moves, new content just appears
 * beneath it, so the sheet's own top edge (and the chevron with it) is what rises as it expands.
 */
@Composable
fun RecapViewBottomSheet(
    name: String,
    isSaved: Boolean,
    includeDateStamp: Boolean,
    onIncludeDateStampChange: (Boolean) -> Unit,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onToggleSavedClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(AppSurfaceVariant)
            .padding(horizontal = 14.dp),
    ) {
        FlatIconButton(
            iconRes = R.drawable.chevron_right,
            contentDescription = if (isExpanded) "Hide options" else "More options",
            onClick = { isExpanded = !isExpanded },
            selected = isExpanded,
            size = 32.dp,
            iconSize = 16.dp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .rotate(if (isExpanded) 90f else -90f),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
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

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.White.copy(alpha = 0.08f), thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "EXPORT OPTIONS",
                    color = Color.White.copy(alpha = 0.45f),
                    fontSize = Typography.Size.extraSmall,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = includeDateStamp,
                        onCheckedChange = onIncludeDateStampChange,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.White,
                            uncheckedColor = Color.White.copy(alpha = 0.4f),
                            checkmarkColor = Color.Black,
                        ),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Date stamp on each clip",
                        color = Color.White,
                        fontSize = Typography.Size.extraSmall,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Preview
@Composable
private fun PreviewCollapsed() {
    val includeDateStamp = remember { mutableStateOf(false) }
    RecapViewBottomSheet(
        name = "Greece summer holiday recap",
        isSaved = false,
        includeDateStamp = includeDateStamp.value,
        onIncludeDateStampChange = { includeDateStamp.value = it },
        canGoBack = true,
        onBack = {},
        onToggleSavedClick = {},
        onShareClick = {},
    )
}

@Preview
@Composable
private fun PreviewSaved() {
    val includeDateStamp = remember { mutableStateOf(true) }
    RecapViewBottomSheet(
        name = "3 - 17 May recap",
        isSaved = true,
        includeDateStamp = includeDateStamp.value,
        onIncludeDateStampChange = { includeDateStamp.value = it },
        canGoBack = true,
        onBack = {},
        onToggleSavedClick = {},
        onShareClick = {},
    )
}
