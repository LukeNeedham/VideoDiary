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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lukeneedham.videodiary.ui.feature.common.Button
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetDefaults
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetLayout
import com.lukeneedham.videodiary.ui.feature.recap.share.model.RecapShareState
import com.lukeneedham.videodiary.ui.theme.Typography
import kotlinx.coroutines.launch

// Big enough for the tallest state's content (SelectingOptions) - every state's content area
// uses this same height, so switching between them never resizes the sheet.
private val SheetContentHeight: Dp = 230.dp

/**
 * The whole share flow (pick options, create the export, share it) as a real Material bottom
 * sheet docked over a recap's video, rather than a separate page. [sheetState] is hoisted to the
 * caller so the share button (which lives in [content], not here) can call `.show()` on it -
 * hiding the sheet (drag handle swipe-down, scrim tap or back) doesn't cancel an in-progress
 * export or lose a finished one, since that state all lives outside this composable.
 *
 * Its content area is a fixed height regardless of [state], so switching between
 * options/progress/ready/failed never changes the sheet's own size - only an explicit user drag
 * does.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecapShareSheet(
    sheetState: ModalBottomSheetState,
    state: RecapShareState,
    includeDateStamp: Boolean,
    onIncludeDateStampChange: (Boolean) -> Unit,
    onCreateClick: () -> Unit,
    onCancelClick: () -> Unit,
    onBackToOptionsClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = SheetDefaults.containerColor,
        sheetShape = SheetDefaults.shape,
        scrimColor = SheetDefaults.scrimColor,
        modifier = modifier,
        sheetContent = {
            SheetLayout(onClose = { scope.launch { sheetState.hide() } }) {
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
        },
        content = content,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun PreviewSelectingOptions() {
    RecapShareSheet(
        sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded),
        state = RecapShareState.SelectingOptions,
        includeDateStamp = false,
        onIncludeDateStampChange = {},
        onCreateClick = {},
        onCancelClick = {},
        onBackToOptionsClick = {},
        onShareClick = {},
        content = {},
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun PreviewInProgress() {
    RecapShareSheet(
        sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded),
        state = MockDataRecapShare.inProgress,
        includeDateStamp = false,
        onIncludeDateStampChange = {},
        onCreateClick = {},
        onCancelClick = {},
        onBackToOptionsClick = {},
        onShareClick = {},
        content = {},
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun PreviewReady() {
    RecapShareSheet(
        sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded),
        state = MockDataRecapShare.ready,
        includeDateStamp = false,
        onIncludeDateStampChange = {},
        onCreateClick = {},
        onCancelClick = {},
        onBackToOptionsClick = {},
        onShareClick = {},
        content = {},
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun PreviewFailed() {
    RecapShareSheet(
        sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded),
        state = MockDataRecapShare.failed,
        includeDateStamp = false,
        onIncludeDateStampChange = {},
        onCreateClick = {},
        onCancelClick = {},
        onBackToOptionsClick = {},
        onShareClick = {},
        content = {},
    )
}
