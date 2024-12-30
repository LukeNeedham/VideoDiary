package com.lukeneedham.videodiary.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import kotlinx.coroutines.launch

class RootViewModel(
    private val settingsDao: SettingsDao,
) : ViewModel() {
    var hasSetupCompleted: Boolean? by mutableStateOf(null)

    init {
        viewModelScope.launch {
            val startDate = settingsDao.getStartDate()
            hasSetupCompleted = startDate != null
        }
    }
}