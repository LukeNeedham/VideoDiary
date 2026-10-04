package com.lukeneedham.videodiary.data.persistence

import com.lukeneedham.videodiary.data.persistence.room.SavedRecapEntity
import com.lukeneedham.videodiary.data.persistence.room.SavedRecapRoomDao
import com.lukeneedham.videodiary.domain.model.SavedRecap
import com.lukeneedham.videodiary.domain.util.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class SavedRecapsDao(
    private val roomDao: SavedRecapRoomDao,
) {
    val allSavedRecaps: Flow<List<SavedRecap>> = roomDao.getAll().map { entities ->
        entities.mapNotNull { entity -> entityToModel(entity) }
    }

    suspend fun saveRecap(
        name: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): String {
        val id = System.currentTimeMillis().toString()
        val entity = SavedRecapEntity(
            id = id,
            name = name,
            startDate = startDate.toString(),
            endDate = endDate.toString(),
            timestampCreated = System.currentTimeMillis(),
        )
        roomDao.insert(entity)
        return id
    }

    suspend fun deleteSavedRecap(id: String) {
        roomDao.deleteById(id)
    }

    private fun entityToModel(entity: SavedRecapEntity): SavedRecap? {
        return try {
            SavedRecap(
                id = entity.id,
                name = entity.name,
                startDate = LocalDate.parse(entity.startDate),
                endDate = LocalDate.parse(entity.endDate),
            )
        } catch (e: Exception) {
            Logger.warning("Failed to parse saved recap: ${entity.id}", e)
            null
        }
    }
}
