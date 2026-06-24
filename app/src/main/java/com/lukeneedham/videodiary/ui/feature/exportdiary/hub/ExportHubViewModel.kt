package com.lukeneedham.videodiary.ui.feature.exportdiary.hub

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SavedExportsDao
import com.lukeneedham.videodiary.domain.model.SavedExport
import kotlinx.coroutines.launch

class ExportHubViewModel(
    private val savedExportsDao: SavedExportsDao,
) : ViewModel() {
    var savedExports: List<SavedExport> by mutableStateOf(emptyList())
        private set

    init {
        viewModelScope.launch {
            savedExportsDao.allSavedExports.collect {
                savedExports = it
            }
        }
    }

    fun deleteExport(id: String) {
        viewModelScope.launch {
            savedExportsDao.deleteSavedExport(id)
        }
    }
}
