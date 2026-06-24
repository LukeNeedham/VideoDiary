package com.lukeneedham.videodiary.domain.model

import java.io.File
import java.time.LocalDate

data class SavedExport(
    val id: String,
    val name: String,
    val videoFile: File,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dayVideoCount: Int,
    val thumbnailFiles: List<File> = emptyList(),
)
