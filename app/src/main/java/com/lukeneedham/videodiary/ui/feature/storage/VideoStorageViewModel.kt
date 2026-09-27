package com.lukeneedham.videodiary.ui.feature.storage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.domain.model.VideoStorageLocation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoStorageViewModel(
    private val settingsDao: SettingsDao,
    private val videosDao: VideosDao,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val location: StateFlow<VideoStorageLocation> = settingsDao.getVideoStorageLocationFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, VideoStorageLocation.Internal)

    val isRemovableStorageAvailable: StateFlow<Boolean> = videosDao.removableStorageAvailable
        .stateIn(viewModelScope, SharingStarted.Eagerly, videosDao.isRemovableStorageAvailable())

    var isChangingLocation by mutableStateOf(false)
        private set

    var error: String? by mutableStateOf(null)
        private set

    fun selectLocation(location: VideoStorageLocation) {
        if (isChangingLocation || location == this.location.value) return

        viewModelScope.launch {
            isChangingLocation = true
            error = null
            val result = withContext(ioDispatcher) {
                videosDao.moveVideosTo(location)
            }
            if (result.isSuccess) {
                settingsDao.setVideoStorageLocation(location)
            } else {
                error = "Could not move videos. Your current storage location has not changed."
            }
            isChangingLocation = false
        }
    }
}
