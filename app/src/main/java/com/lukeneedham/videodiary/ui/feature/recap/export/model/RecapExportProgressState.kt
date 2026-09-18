package com.lukeneedham.videodiary.ui.feature.recap.export.model

sealed interface RecapExportProgressState {
    data class InProgress(val progressFraction: Float) : RecapExportProgressState
    data class Failed(val error: String) : RecapExportProgressState
}
