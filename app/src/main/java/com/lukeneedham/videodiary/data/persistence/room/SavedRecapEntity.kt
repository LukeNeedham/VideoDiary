package com.lukeneedham.videodiary.data.persistence.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_recaps")
data class SavedRecapEntity(
    @PrimaryKey val id: String,
    val name: String,
    /** ISO-8601 date (e.g. "2024-06-01") */
    val startDate: String,
    /** ISO-8601 date (e.g. "2024-06-15") */
    val endDate: String,
    @ColumnInfo(defaultValue = "0")
    val timestampCreated: Long = 0,
)
