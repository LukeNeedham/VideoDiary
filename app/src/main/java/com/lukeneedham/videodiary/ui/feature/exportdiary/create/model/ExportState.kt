package com.lukeneedham.videodiary.ui.feature.exportdiary.create.model

sealed interface ExportState {
    object Ready : ExportState
    data class Failed(val error: String) : ExportState
    data class InProgress(val progressFraction: Float) : ExportState
}