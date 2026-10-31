package com.lukeneedham.videodiary.ui.feature.recap.share

import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetDefaults
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetLayout
import com.lukeneedham.videodiary.ui.feature.recap.share.model.RecapShareState
import com.lukeneedham.videodiary.ui.theme.AppTheme
import kotlinx.coroutines.launch

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
                RecapShareSheetContent(
                    state = state,
                    includeDateStamp = includeDateStamp,
                    onIncludeDateStampChange = onIncludeDateStampChange,
                    onCreateClick = onCreateClick,
                    onCancelClick = onCancelClick,
                    onBackToOptionsClick = onBackToOptionsClick,
                    onShareClick = onShareClick,
                )
            }
        },
        content = content,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
private fun Preview() {
    AppTheme {
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
}
