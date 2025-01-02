package com.lukeneedham.videodiary.ui.root

sealed interface RootState {
    object Loading : RootState
    object NeedsPermissions : RootState
    object NeedsSetup : RootState
    object Ready : RootState
}