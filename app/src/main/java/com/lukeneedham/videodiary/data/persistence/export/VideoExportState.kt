package com.lukeneedham.videodiary.data.persistence.export

import java.io.File

sealed interface VideoExportState {
    data class InProgress(val progressFraction: Float) : VideoExportState
    data class Success(val outputFile: File) : VideoExportState
    data class Failure(val error: Exception) : VideoExportState
}