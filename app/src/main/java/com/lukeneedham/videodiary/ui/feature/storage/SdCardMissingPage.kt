package com.lukeneedham.videodiary.ui.feature.storage

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

/**
 * Shown instead of the whole app when videos are stored on the SD card and it is not there.
 * Back closes the app: there is nowhere else to go.
 */
@Composable
fun SdCardMissingPage(
    canMockReinsert: Boolean,
    onMockReinsertClick: () -> Unit,
    onClose: () -> Unit,
) {
    BackHandler(onBack = onClose)
    SdCardMissingPageContent(
        canMockReinsert = canMockReinsert,
        onMockReinsertClick = onMockReinsertClick,
    )
}
