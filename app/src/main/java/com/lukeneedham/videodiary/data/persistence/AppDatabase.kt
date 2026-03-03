package com.lukeneedham.videodiary.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukeneedham.videodiary.data.persistence.savedexport.SavedExportEntity
import com.lukeneedham.videodiary.data.persistence.savedexport.SavedExportRoomDao

@Database(entities = [SavedExportEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedExportDao(): SavedExportRoomDao
}
