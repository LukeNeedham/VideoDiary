package com.lukeneedham.videodiary.ui.root

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

    var orientationState: RootOrientationState by mutableStateOf(RootOrientationState.Loading)

    init {
        viewModelScope.launch {
            val requiredSetupData = listOf(
                settingsDao.getResolution(),
                settingsDao.getResolutionRotation(),
                settingsDao.getVideoDuration(),
            )
            hasSetupCompleted = requiredSetupData.all { it != null }
        }
        viewModelScope.launch {
            settingsDao.getOrientationFlow().collect { orientation ->
                orientationState = if (orientation == null) {
                    RootOrientationState.NeedsSetup
                } else {
                    RootOrientationState.Ready(orientation)
                }
            }
        }
    }
}