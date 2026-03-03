package com.lukeneedham.videodiary.data.persistence

import android.content.Context
import com.lukeneedham.videodiary.domain.model.SavedExport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.time.LocalDate

class SavedExportsDao(
    private val context: Context,
) {
    private val savedExportsDir = File(context.filesDir, "saved_exports").apply {
        mkdirs()
    }

    private val allSavedExportsMutable = MutableStateFlow(loadAllSavedExports())
    val allSavedExports = allSavedExportsMutable.asStateFlow()

    fun saveExport(
        sourceFile: File,
        name: String,
        startDate: LocalDate,
        endDate: LocalDate,
        dayVideoCount: Int,
    ) {
        val destinationFile = File(savedExportsDir, "$name.mp4")
        if (destinationFile.exists()) {
            destinationFile.delete()
        }
        sourceFile.copyTo(destinationFile)
        
        // In a real app we'd save metadata to a DB or sidecar file. 
        // For simplicity here, we'll just name the file.
        // If we want more metadata, we can use a JSON file or similar.
        refreshSavedExportsState()
    }

    fun deleteExport(export: SavedExport) {
        export.file.delete()
        refreshSavedExportsState()
    }

    private fun refreshSavedExportsState() {
        allSavedExportsMutable.value = loadAllSavedExports()
    }

    private fun loadAllSavedExports(): List<SavedExport> {
        val files = savedExportsDir.listFiles { _, name -> name.endsWith(".mp4") } ?: return emptyList()
        return files.map { file ->
            SavedExport(
                name = file.nameWithoutExtension,
                file = file,
                // We don't have these for now, so use placeholders or extract from some metadata file
                startDate = LocalDate.MIN,
                endDate = LocalDate.MAX,
                dayVideoCount = 0,
            )
        }.sortedByDescending { it.file.lastModified() }
    }
}
