package com.lukeneedham.videodiary.ui.feature.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.data.repository.MockDataRepository
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
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val allowRetakeForPastDays: StateFlow<Boolean> = settingsDao.getAllowEditPastDaysFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val fakeSdCardEnabled: StateFlow<Boolean> = settingsDao.getDebugFakeSdCardEnabledFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val fakeSdCardInserted: StateFlow<Boolean> = settingsDao.getDebugFakeSdCardInsertedFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val slowVideoMoves: StateFlow<Boolean> = settingsDao.getDebugSlowVideoMovesFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun fillWithMockData() {
        mockDataRepository.fillWithMockData()
    }

    fun setAllowRetakeForPastDays(allow: Boolean) {
        viewModelScope.launch {
            settingsDao.setDebugAllowRetakeForPastDays(allow)
        }
    }

    fun setFakeSdCardEnabled(enabled: Boolean) {
        applyStorageSetting { settingsDao.setDebugFakeSdCardEnabled(enabled) }
    }

    fun setFakeSdCardInserted(inserted: Boolean) {
        applyStorageSetting { settingsDao.setDebugFakeSdCardInserted(inserted) }
    }

    fun setSlowVideoMoves(slow: Boolean) {
        applyStorageSetting { settingsDao.setDebugSlowVideoMoves(slow) }
    }

    /** Saves the setting, then makes the diary react to it, as if the SD card had changed */
    private fun applyStorageSetting(save: suspend () -> Unit) {
        viewModelScope.launch {
            save()
            withContext(ioDispatcher) {
                videosDao.initialiseStorageLocation()
            }
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
