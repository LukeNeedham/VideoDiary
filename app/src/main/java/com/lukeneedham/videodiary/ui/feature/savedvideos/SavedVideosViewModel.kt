package com.lukeneedham.videodiary.ui.feature.savedvideos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SavedExportsDao
import com.lukeneedham.videodiary.domain.model.SavedExport
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SavedVideosViewModel(
    private val savedExportsDao: SavedExportsDao,
) : ViewModel() {
    val savedVideos = savedExportsDao.allSavedExports.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    fun deleteExport(export: SavedExport) {
        savedExportsDao.deleteExport(export)
    }
}
