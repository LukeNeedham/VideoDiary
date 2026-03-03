package com.lukeneedham.videodiary.data.persistence.savedexport

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedExportRoomDao {
    @Query("SELECT * FROM saved_exports ORDER BY lastModified DESC")
    fun getAll(): Flow<List<SavedExportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SavedExportEntity)

    @Delete
    suspend fun delete(entity: SavedExportEntity)

    @Query("DELETE FROM saved_exports WHERE id = :id")
    suspend fun deleteById(id: String)
}
