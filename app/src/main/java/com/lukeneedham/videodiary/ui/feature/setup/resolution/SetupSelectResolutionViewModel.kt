package com.lukeneedham.videodiary.ui.feature.setup.resolution

import android.util.Size
import androidx.lifecycle.ViewModel
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.domain.model.CameraResolutionRotation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SetupSelectResolutionViewModel(
    private val settingsDao: SettingsDao,
    ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val saveSettingsScope = CoroutineScope(ioDispatcher)

    private val onSavedEventMutable = MutableSharedFlow<Unit>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onSavedEventFlow = onSavedEventMutable.asSharedFlow()

    fun saveSettings(resolution: Size, rotation: CameraResolutionRotation) {
        saveSettingsScope.launch {
            listOf(
                async { settingsDao.setResolution(resolution) },
                async { settingsDao.setResolutionRotation(rotation) },
            ).awaitAll()
            onSavedEventMutable.emit(Unit)
        }
    }
}