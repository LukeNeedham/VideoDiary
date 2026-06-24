package com.lukeneedham.videodiary.data.persistence.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_exports")
data class SavedExportEntity(
    @PrimaryKey val id: String,
    val name: String,
    val includedDates: String,
)
