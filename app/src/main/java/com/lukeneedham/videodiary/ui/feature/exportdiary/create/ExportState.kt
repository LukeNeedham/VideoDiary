package com.lukeneedham.videodiary.ui.feature.exportdiary.create

sealed interface ExportState {
    object Ready : ExportState
    data class Failed(val error: String) : ExportState
    object InProgress : ExportState
}