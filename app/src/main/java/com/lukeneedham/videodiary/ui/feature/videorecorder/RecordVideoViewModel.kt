package com.lukeneedham.videodiary.ui.feature.videorecorder

import android.util.Size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import kotlinx.coroutines.launch

class RecordVideoViewModel(
    private val settingsDao: SettingsDao,
) : ViewModel() {
    var resolution: Size? by mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            resolution = settingsDao.getResolution()
        }
    }
}