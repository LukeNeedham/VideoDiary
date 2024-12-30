package com.lukeneedham.videodiary.ui.feature.exportdiary

import java.io.File

sealed interface ExportState {
    object Ready : ExportState
    object NoVideos : ExportState
    object InProgress : ExportState
    data class Success(val exportedFile: File) : ExportState
}