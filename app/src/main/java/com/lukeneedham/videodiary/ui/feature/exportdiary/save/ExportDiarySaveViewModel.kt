package com.lukeneedham.videodiary.ui.feature.exportdiary.save

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SavedExportsDao
import com.lukeneedham.videodiary.domain.model.ExportedVideo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class ExportDiarySaveViewModel(
    private val exportedVideo: ExportedVideo,
    private val savedExportsDao: SavedExportsDao,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    var name: String by mutableStateOf("")
        private set

    var isSaving: Boolean by mutableStateOf(false)
        private set

    fun setExportName(name: String) {
        this.name = name
    }

    fun save(onSaved: () -> Unit) {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) return
        if (isSaving) return

        isSaving = true
        viewModelScope.launch(ioDispatcher) {
            savedExportsDao.saveExport(trimmedName, exportedVideo)
            isSaving = false
            onSaved()
        }
    }
}
