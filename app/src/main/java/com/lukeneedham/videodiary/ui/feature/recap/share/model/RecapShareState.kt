package com.lukeneedham.videodiary.ui.feature.recap.share.model

import java.io.File

sealed interface RecapShareState {
    data object SelectingOptions : RecapShareState
    data class InProgress(val progressFraction: Float) : RecapShareState
    data class Ready(val outputFile: File) : RecapShareState
    data class Failed(val error: String) : RecapShareState
}
