package com.lukeneedham.videodiary.ui.root

sealed interface RootState {
    object Loading : RootState
    object NeedsPermissions : RootState
    /** The app is set to store videos on the SD card, but it is not there. Nothing else works. */
    object SdCardMissing : RootState
    object NeedsSetup : RootState
    object Ready : RootState
}