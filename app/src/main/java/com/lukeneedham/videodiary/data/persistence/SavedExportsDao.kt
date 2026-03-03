package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import com.lukeneedham.videodiary.data.persistence.savedexport.SavedExportEntity
import com.lukeneedham.videodiary.data.persistence.savedexport.SavedExportMetadata
import com.lukeneedham.videodiary.data.persistence.savedexport.SavedExportRoomDao
import com.lukeneedham.videodiary.domain.model.SavedExport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import java.time.LocalDate

class SavedExportsDao(
    private val context: Context,
    private val roomDao: SavedExportRoomDao,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val savedExportsDir = File(context.filesDir, "saved_exports").apply {
        mkdirs()
    }

    val allSavedExports: StateFlow<List<SavedExport>> = roomDao.getAll().map { entities ->
        entities.map { entity ->
            SavedExport(
                id = entity.id,
                name = entity.name,
                file = File(savedExportsDir, "${entity.name}.mp4"),
                startDate = LocalDate.ofEpochDay(entity.startDateEpochDay),
                endDate = LocalDate.ofEpochDay(entity.endDateEpochDay),
                dayVideoCount = entity.dayVideoCount,
            )
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun saveExport(
        sourceFile: File,
        name: String,
        startDate: LocalDate,
        endDate: LocalDate,
        dayVideoCount: Int,
    ) {
        val id = UUID.randomUUID().toString()
        val destinationFile = File(savedExportsDir, "$id.mp4")
        sourceFile.copyTo(destinationFile)

        scope.launch {
            val entity = SavedExportEntity(
                id = id,
                name = name,
                startDateEpochDay = startDate.toEpochDay(),
                endDateEpochDay = endDate.toEpochDay(),
                dayVideoCount = dayVideoCount,
                lastModified = System.currentTimeMillis()
            )
            roomDao.insert(entity)
        }
    }

    fun deleteExport(export: SavedExport) {
        export.file.delete()
        scope.launch {
            roomDao.deleteById(export.id)
        }
    }

    private fun getMetadataFile(name: String) = File(savedExportsDir, "$name.metadata")
}
