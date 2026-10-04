package com.lukeneedham.videodiary.data.persistence.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedRecapRoomDao {
    @Query("SELECT * FROM saved_recaps ORDER BY timestampCreated DESC")
    fun getAll(): Flow<List<SavedRecapEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SavedRecapEntity)

    @Query("DELETE FROM saved_recaps WHERE id = :id")
    suspend fun deleteById(id: String)
}
