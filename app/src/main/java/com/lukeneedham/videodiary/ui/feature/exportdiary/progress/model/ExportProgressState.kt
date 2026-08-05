package com.lukeneedham.videodiary.ui.feature.exportdiary.progress.model

sealed interface ExportProgressState {
    data class InProgress(val progressFraction: Float) : ExportProgressState
    data class Failed(val error: String) : ExportProgressState
}
