package com.lukeneedham.videodiary.data.persistence.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedRecapEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedRecapDao(): SavedRecapRoomDao

    companion object {
        fun create(context: Context): AppDatabase =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "video_diary_db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
    }
}
