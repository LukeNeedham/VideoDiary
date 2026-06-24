package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import com.lukeneedham.videodiary.domain.model.SavedExport
import com.lukeneedham.videodiary.domain.util.logger.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.time.LocalDate

class SavedExportsDao(
    context: Context,
) {
    private val savedExportsDir = File(context.filesDir, "saved_exports").apply {
        mkdirs()
    }

    private val allSavedExportsMutable = MutableStateFlow(loadAll())
    val allSavedExports = allSavedExportsMutable.asStateFlow()

    fun saveExport(name: String, exportedVideo: ExportedVideo): SavedExport {
        val id = System.currentTimeMillis().toString()
        val exportDir = File(savedExportsDir, id).apply { mkdirs() }

        val videoFile = File(exportDir, VIDEO_FILE_NAME)
        exportedVideo.videoFile.copyTo(videoFile, overwrite = true)

        val metadataFile = File(exportDir, METADATA_FILE_NAME)
        metadataFile.writeText(
            listOf(
                name,
                exportedVideo.startDate.toString(),
                exportedVideo.endDate.toString(),
                exportedVideo.dayVideoCount.toString(),
            ).joinToString("\n")
        )

        val savedExport = SavedExport(
            id = id,
            name = name,
            videoFile = videoFile,
            startDate = exportedVideo.startDate,
            endDate = exportedVideo.endDate,
            dayVideoCount = exportedVideo.dayVideoCount,
        )
        refreshState()
        return savedExport
    }

    fun deleteSavedExport(id: String) {
        val exportDir = File(savedExportsDir, id)
        exportDir.deleteRecursively()
        refreshState()
    }

    private fun refreshState() {
        allSavedExportsMutable.value = loadAll()
    }

    private fun loadAll(): List<SavedExport> {
        val dirs = savedExportsDir.listFiles()?.filter { it.isDirectory } ?: return emptyList()
        return dirs.mapNotNull { dir -> loadExport(dir) }
            .sortedByDescending { it.id }
    }

    private fun loadExport(dir: File): SavedExport? {
        val id = dir.name
        val videoFile = File(dir, VIDEO_FILE_NAME)
        val metadataFile = File(dir, METADATA_FILE_NAME)

        if (!videoFile.exists() || !metadataFile.exists()) {
            Logger.warning("Skipping saved export with missing files: $id")
            return null
        }

        return try {
            val lines = metadataFile.readLines()
            SavedExport(
                id = id,
                name = lines[0],
                videoFile = videoFile,
                startDate = LocalDate.parse(lines[1]),
                endDate = LocalDate.parse(lines[2]),
                dayVideoCount = lines[3].toInt(),
            )
        } catch (e: Exception) {
            Logger.warning("Skipping saved export with invalid metadata: $id", e)
            null
        }
    }

    companion object {
        private const val VIDEO_FILE_NAME = "video.mp4"
        private const val METADATA_FILE_NAME = "metadata.txt"
    }
}
