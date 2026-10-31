package com.lukeneedham.videodiary.ui.feature.recap.share

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.recap.share.model.RecapShareState
import com.lukeneedham.videodiary.ui.theme.AppTheme
import com.lukeneedham.videodiary.ui.theme.Typography

// Big enough for the tallest state's content (SelectingOptions) - every state's content area
// uses this same height, so switching between them never resizes the sheet.
private val SheetContentHeight: Dp = 230.dp

/**
 * What the share sheet shows for the [state] of the export: the options, the progress, the finished export or the error.
 * Its area is a fixed height whatever the state, so switching state never resizes the sheet.
 */
@Composable
internal fun RecapShareSheetContent(
    state: RecapShareState,
    includeDateStamp: Boolean,
    onIncludeDateStampChange: (Boolean) -> Unit,
    onCreateClick: () -> Unit,
    onCancelClick: () -> Unit,
    onBackToOptionsClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(SheetContentHeight)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when (state) {
                RecapShareState.SelectingOptions -> {
                    Text(
                        text = "Share recap",
                        color = Color.White,
                        fontSize = Typography.Size.big,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

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

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        text = "Create video",
                        onClick = onCreateClick,
                        backgroundColor = Color.White,
                        foregroundColor = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                is RecapShareState.InProgress -> {
                    Text(
                        text = "Preparing your recap...",
                        color = Color.White,
                        fontSize = Typography.Size.medium,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    LinearProgressIndicator(
                        progress = state.progressFraction,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        text = "Cancel",
                        onClick = onCancelClick,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                is RecapShareState.Ready -> {
                    Text(
                        text = "Your recap is ready to share",
                        color = Color.White,
                        fontSize = Typography.Size.medium,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        text = "Share",
                        onClick = onShareClick,
                        backgroundColor = Color.White,
                        foregroundColor = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        text = "New export",
                        onClick = onBackToOptionsClick,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                is RecapShareState.Failed -> {
                    Text(
                        text = "Something went wrong while preparing your recap: ${state.error}",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        text = "Try again",
                        onClick = onBackToOptionsClick,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        RecapShareSheetContent(
            state = RecapShareState.SelectingOptions,
            includeDateStamp = false,
            onIncludeDateStampChange = {},
            onCreateClick = {},
            onCancelClick = {},
            onBackToOptionsClick = {},
            onShareClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewInProgress() {
    AppTheme {
        RecapShareSheetContent(
            state = MockDataRecapShare.inProgress,
            includeDateStamp = false,
            onIncludeDateStampChange = {},
            onCreateClick = {},
            onCancelClick = {},
            onBackToOptionsClick = {},
            onShareClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewReady() {
    AppTheme {
        RecapShareSheetContent(
            state = MockDataRecapShare.ready,
            includeDateStamp = false,
            onIncludeDateStampChange = {},
            onCreateClick = {},
            onCancelClick = {},
            onBackToOptionsClick = {},
            onShareClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewFailed() {
    AppTheme {
        RecapShareSheetContent(
            state = MockDataRecapShare.failed,
            includeDateStamp = false,
            onIncludeDateStampChange = {},
            onCreateClick = {},
            onCancelClick = {},
            onBackToOptionsClick = {},
            onShareClick = {},
        )
    }
}
