package com.lukeneedham.videodiary.ui.feature.setup.storage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lukeneedham.videodiary.data.persistence.VideosDao
import com.lukeneedham.videodiary.domain.model.VideoStorageLocation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SetupSelectStorageViewModel(
    private val videosDao: VideosDao,
    ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val saveSettingsScope = CoroutineScope(ioDispatcher)

    private val onSavedEventMutable = MutableSharedFlow<Unit>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onSavedEventFlow = onSavedEventMutable.asSharedFlow()

    /**
     * Whether there is an SD card to choose. Read once, when onboarding starts: the storage page
     * is only part of onboarding if this is true, and the pages must not change part way through.
     */
    val isRemovableStorageAvailable: Boolean = videosDao.isRemovableStorageAvailable()

    var location: VideoStorageLocation by mutableStateOf(VideoStorageLocation.Internal)
        private set

    var error: String? by mutableStateOf(null)
        private set

    val options = listOf(VideoStorageLocation.Internal, VideoStorageLocation.RemovableStorage)

    fun onLocationChange(newLocation: VideoStorageLocation) {
        location = newLocation
        error = null
    }

    fun saveSettings() {
        saveSettingsScope.launch {
            val result = videosDao.moveVideosTo(location)
            if (result.isSuccess) {
                onSavedEventMutable.emit(Unit)
            } else {
                error = "Could not use the SD card. Check that it is inserted, or choose internal storage."
            }
        }
    }
}
