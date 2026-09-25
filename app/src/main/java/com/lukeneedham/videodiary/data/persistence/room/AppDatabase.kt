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
                // The saved-export table was replaced by saved-recap (a plain date range,
                // no video file); there's no meaningful migration from the old shape.
                .fallbackToDestructiveMigration()
                .build()
    }
}
