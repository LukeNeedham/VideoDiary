package com.lukeneedham.videodiary.data.persistence

import android.content.Context
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
) {
    private val savedExportsDir = File(context.filesDir, "saved_exports").apply {
        mkdirs()
    }

    val allSavedExports: Flow<List<SavedExport>> = roomDao.getAll().map { entities ->
        entities.mapNotNull { entity -> entityToModel(entity) }
    }

    suspend fun saveExport(
        name: String,
        exportedVideo: ExportedVideo,
        thumbnailFiles: List<File>,
    ) {
        val id = System.currentTimeMillis().toString()
        val videoFileName = "$id.mp4"
        val videoFile = File(savedExportsDir, videoFileName)

        exportedVideo.videoFile.copyTo(videoFile, overwrite = true)

        val thumbnailFileNames = thumbnailFiles.mapIndexedNotNull { index, source ->
            if (!source.exists()) return@mapIndexedNotNull null
            val thumbName = "${id}_thumb_$index.jpg"
            source.copyTo(File(savedExportsDir, thumbName), overwrite = true)
            thumbName
        }

        val entity = SavedExportEntity(
            id = id,
            name = name,
            videoFileName = videoFileName,
            startDate = exportedVideo.startDate.toString(),
            endDate = exportedVideo.endDate.toString(),
            dayVideoCount = exportedVideo.dayVideoCount,
            thumbnailFileNames = thumbnailFileNames.joinToString(","),
        )
        roomDao.insert(entity)
    }

    suspend fun deleteSavedExport(id: String) {
        roomDao.deleteById(id)
        val files = savedExportsDir.listFiles() ?: return
        files.filter { it.name.startsWith(id) }.forEach { it.delete() }
    }

    private fun entityToModel(entity: SavedExportEntity): SavedExport? {
        val videoFile = File(savedExportsDir, entity.videoFileName)
        if (!videoFile.exists()) {
            Logger.warning("Saved export video file missing: ${entity.videoFileName}")
            return null
        }
        return try {
            val thumbnailFiles = if (entity.thumbnailFileNames.isNotEmpty()) {
                entity.thumbnailFileNames.split(",").mapNotNull { thumbName ->
                    val thumbFile = File(savedExportsDir, thumbName)
                    if (thumbFile.exists()) thumbFile else null
                }
            } else {
                emptyList()
            }

            SavedExport(
                id = entity.id,
                name = entity.name,
                videoFile = videoFile,
                startDate = LocalDate.parse(entity.startDate),
                endDate = LocalDate.parse(entity.endDate),
                dayVideoCount = entity.dayVideoCount,
                thumbnailFiles = thumbnailFiles,
            )
        } catch (e: Exception) {
            Logger.warning("Failed to parse saved export: ${entity.id}", e)
            null
        }
    }
}
