package com.lukeneedham.videodiary.ui.root

import com.lukeneedham.videodiary.domain.model.Orientation

sealed interface RootOrientationState {
    object Loading : RootOrientationState
    object NeedsSetup : RootOrientationState
    data class Ready(val orientation: Orientation) : RootOrientationState
}