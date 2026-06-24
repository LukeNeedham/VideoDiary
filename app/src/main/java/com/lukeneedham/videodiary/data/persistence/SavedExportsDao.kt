package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import com.lukeneedham.videodiary.data.mapper.ThumbnailFileNameMapper
import com.lukeneedham.videodiary.data.persistence.room.SavedExportEntity
import com.lukeneedham.videodiary.data.persistence.room.SavedExportRoomDao
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import com.lukeneedham.videodiary.domain.model.SavedExport
import com.lukeneedham.videodiary.domain.util.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.time.LocalDate

class SavedExportsDao(
    context: Context,
    private val roomDao: SavedExportRoomDao,
    private val thumbnailFileNameMapper: ThumbnailFileNameMapper,
) {
    private val savedExportsDir = File(context.filesDir, "saved_exports").apply {
        mkdirs()
    }

    private val thumbnailsDir = File(context.filesDir, "thumbnails")

    val allSavedExports: Flow<List<SavedExport>> = roomDao.getAll().map { entities ->
        entities.mapNotNull { entity -> entityToModel(entity) }
    }

    suspend fun saveExport(
        name: String,
        exportedVideo: ExportedVideo,
        includedDates: List<LocalDate>,
    ) {
        val id = System.currentTimeMillis().toString()
        val videoFile = File(savedExportsDir, "$id.mp4")
        exportedVideo.videoFile.copyTo(videoFile, overwrite = true)

        val entity = SavedExportEntity(
            id = id,
            name = name,
            includedDates = includedDates.joinToString(","),
        )
        roomDao.insert(entity)
    }

    suspend fun deleteSavedExport(id: String) {
        roomDao.deleteById(id)
        File(savedExportsDir, "$id.mp4").delete()
    }

    private fun entityToModel(entity: SavedExportEntity): SavedExport? {
        val videoFile = File(savedExportsDir, "${entity.id}.mp4")
        if (!videoFile.exists()) {
            Logger.warning("Saved export video file missing: ${entity.id}")
            return null
        }
        return try {
            val dates = entity.includedDates.split(",").map { LocalDate.parse(it) }

            val thumbnailFiles = dates.mapNotNull { date ->
                val file = File(thumbnailsDir, thumbnailFileNameMapper.dateToName(date))
                if (file.exists()) file else null
            }

            SavedExport(
                id = entity.id,
                name = entity.name,
                videoFile = videoFile,
                startDate = dates.min(),
                endDate = dates.max(),
                dayVideoCount = dates.size,
                thumbnailFiles = thumbnailFiles,
            )
        } catch (e: Exception) {
            Logger.warning("Failed to parse saved export: ${entity.id}", e)
            null
        }
    }
}
