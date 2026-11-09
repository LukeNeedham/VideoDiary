package com.lukeneedham.videodiary.ui.feature.setup.resolution

import android.util.Size
import androidx.lifecycle.ViewModel
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.data.repository.VideoResolutionRepository
import com.lukeneedham.videodiary.domain.model.CameraResolutionRotation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SetupSelectResolutionViewModel(
    private val settingsDao: SettingsDao,
    private val videoResolutionRepository: VideoResolutionRepository,
    ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val saveSettingsScope = CoroutineScope(ioDispatcher)

    private val onSavedEventChannel = Channel<Unit>(Channel.BUFFERED)
    val onSavedEventFlow = onSavedEventChannel.receiveAsFlow()

    fun saveSettings(resolution: Size, rotation: CameraResolutionRotation) {
        saveSettingsScope.launch {
            listOf(
                async { settingsDao.setResolution(resolution) },
                async { settingsDao.setResolutionRotation(rotation) },
            ).awaitAll()
            videoResolutionRepository.clearCache()
            onSavedEventChannel.send(Unit)
        }
    }
}