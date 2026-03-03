package com.lukeneedham.videodiary.data.persistence.savedexport

data class SavedExportMetadata(
    val name: String,
    val startDateEpochDay: Long,
    val endDateEpochDay: Long,
    val dayVideoCount: Int,
    val createdAtMillis: Long,
)
