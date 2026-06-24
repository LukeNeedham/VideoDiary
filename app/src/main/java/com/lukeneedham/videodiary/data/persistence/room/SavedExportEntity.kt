package com.lukeneedham.videodiary.data.persistence.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_exports")
data class SavedExportEntity(
    @PrimaryKey val id: String,
    val name: String,
    /** Comma-separated ISO-8601 dates (e.g. "2024-06-01,2024-06-15") of the diary days included in this export. */
    val includedDates: String,
)
