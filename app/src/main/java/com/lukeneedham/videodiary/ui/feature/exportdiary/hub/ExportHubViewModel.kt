package com.lukeneedham.videodiary.ui.feature.exportdiary.hub

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SavedExportsDao
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.domain.model.SavedExport
import kotlinx.coroutines.launch
import java.io.File

data class SavedExportWithThumbnails(
    val export: SavedExport,
    val thumbnailFiles: List<File>,
)

class ExportHubViewModel(
    private val savedExportsDao: SavedExportsDao,
    private val videosDao: VideosDao,
) : ViewModel() {
    var savedExports: List<SavedExportWithThumbnails> by mutableStateOf(emptyList())
        private set

    init {
        viewModelScope.launch {
            savedExportsDao.allSavedExports.collect { exports ->
                savedExports = exports.map { export ->
                    val thumbnails = export.includedDates.mapNotNull { date ->
                        videosDao.getThumbnailFileIfExists(date)
                    }
                    SavedExportWithThumbnails(
                        export = export,
                        thumbnailFiles = thumbnails,
                    )
                }
            }
        }
    }

    fun deleteExport(id: String) {
        viewModelScope.launch {
            savedExportsDao.deleteSavedExport(id)
        }
    }
}
