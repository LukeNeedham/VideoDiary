package com.lukeneedham.videodiary.ui.feature.recap.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetDefaults
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetLayout
import com.lukeneedham.videodiary.ui.theme.AppSurfaceVariant
import com.lukeneedham.videodiary.ui.theme.Typography
import kotlinx.coroutines.launch

private const val GRID_COLUMNS = 2

private enum class RecapCreateType(val title: String, val description: String) {
    Week("Week", "Seven days at a time"),
    Month("Month", "A whole month, start to finish"),
    Year("Year", "Your entire year in one video"),
    Custom("Custom", "Pick your own dates and name it"),
}

/** A bottom sheet for choosing which type of recap to create, shown as a grid of options. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecapCreateTypeSheet(
    onWeekClick: () -> Unit,
    onMonthClick: () -> Unit,
    onYearClick: () -> Unit,
    onCustomClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SheetDefaults.containerColor,
        contentColor = SheetDefaults.contentColor,
        shape = SheetDefaults.shape,
        scrimColor = SheetDefaults.scrimColor,
        dragHandle = null,
        // Handled by SheetLayout, so that the sheet can animate fully out
        windowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        SheetLayout(
            onClose = {
                scope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
            },
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(bottom = 4.dp),
                ) {
                    Text(
                        text = "Create a recap",
                        color = Color.White,
                        fontSize = Typography.Size.big,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Combine your daily videos into a single video. Choose the period to include.",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = Typography.Size.small,
                    )
                }
                RecapCreateType.entries.chunked(GRID_COLUMNS).forEach { rowTypes ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                    ) {
                        rowTypes.forEach { type ->
                            RecapCreateTypeItem(
                                type = type,
                                onClick = {
                                    // Dismiss first, so the sheet is not still open when coming back
                                    onDismiss()
                                    when (type) {
                                        RecapCreateType.Week -> onWeekClick()
                                        RecapCreateType.Month -> onMonthClick()
                                        RecapCreateType.Year -> onYearClick()
                                        RecapCreateType.Custom -> onCustomClick()
                                    }
                                },
                            )
                        }
                        repeat(GRID_COLUMNS - rowTypes.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.RecapCreateTypeItem(
    type: RecapCreateType,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .background(AppSurfaceVariant)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 16.dp),
    ) {
        Text(
            text = type.title,
            color = Color.White,
            fontSize = Typography.Size.medium,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = type.description,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = Typography.Size.extraSmall,
            textAlign = TextAlign.Start,
        )
    }
}

@Preview
@Composable
private fun PreviewRecapCreateTypeSheet() {
    RecapCreateTypeSheet(
        onWeekClick = {},
        onMonthClick = {},
        onYearClick = {},
        onCustomClick = {},
        onDismiss = {},
    )
}
