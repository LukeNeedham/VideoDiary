package com.lukeneedham.videodiary.ui.feature.debug

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.data.repository.MockDataRepository
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DebugViewModel(
    private val mockDataRepository: MockDataRepository,
    private val settingsDao: SettingsDao,
    private val videosDao: VideosDao,
    private val videoResolutionRepository: VideoResolutionRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    var videoAspectRatio: Float? by mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            videoAspectRatio = videoResolutionRepository.getAspectRatio()
        }
    }

    val allowRetakeForPastDays: StateFlow<Boolean> = settingsDao.getAllowEditPastDaysFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun fillWithMockData() {
        mockDataRepository.fillWithMockData()
    }

    fun setAllowRetakeForPastDays(allow: Boolean) {
        viewModelScope.launch {
            settingsDao.setDebugAllowRetakeForPastDays(allow)
        }
    }

    fun resyncThumbnails() {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                videosDao.resyncAllThumbnails()
            }
        }
    }
}
