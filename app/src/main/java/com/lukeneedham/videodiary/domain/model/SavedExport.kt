package com.lukeneedham.videodiary.domain.model

import java.io.File
import java.time.LocalDate

data class SavedExport(
    val id: String,
    val name: String,
    val file: File,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val dayVideoCount: Int,
)
