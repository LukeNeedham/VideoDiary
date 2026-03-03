package com.lukeneedham.videodiary.data.persistence.savedexport

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_exports")
data class SavedExportEntity(
    @PrimaryKey val id: String,
    val name: String,
    val startDateEpochDay: Long,
    val endDateEpochDay: Long,
    val dayVideoCount: Int,
    val lastModified: Long,
)
