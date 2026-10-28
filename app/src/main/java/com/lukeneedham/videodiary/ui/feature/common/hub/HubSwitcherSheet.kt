package com.lukeneedham.videodiary.ui.feature.common.hub

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetDefaults
import com.lukeneedham.videodiary.ui.feature.common.sheet.SheetLayout
import com.lukeneedham.videodiary.ui.theme.AppTheme

/**
 * The hub switcher bottom sheet, docked over the [content] (the whole app flow): a grid of all hubs, highlighting the
 * [currentHub]. [sheetState] is hoisted to the caller so it can show and hide the sheet.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HubSwitcherSheet(
    sheetState: ModalBottomSheetState,
    currentHub: Hub?,
    onHubClick: (Hub) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = SheetDefaults.containerColor,
        sheetShape = SheetDefaults.shape,
        scrimColor = SheetDefaults.scrimColor,
        modifier = modifier,
        sheetContent = {
            SheetLayout(onClose = onClose) {
                HubSwitcherSheetContent(currentHub = currentHub, onHubClick = onHubClick)
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
        HubSwitcherSheet(
            sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded),
            currentHub = Hub.Calendar,
            onHubClick = {},
            onClose = {},
            content = {},
        )
    }
}
