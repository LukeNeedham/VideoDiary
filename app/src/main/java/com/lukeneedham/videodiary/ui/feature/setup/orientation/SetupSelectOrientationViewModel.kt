package com.lukeneedham.videodiary.ui.feature.setup.orientation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import com.lukeneedham.videodiary.domain.model.Orientation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SetupSelectOrientationViewModel(
    private val settingsDao: SettingsDao,
    ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val saveSettingsScope = CoroutineScope(ioDispatcher)

    private val onSavedEventMutable = MutableSharedFlow<Unit>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onSavedEventFlow = onSavedEventMutable.asSharedFlow()

    var orientation: Orientation by mutableStateOf(Orientation.Portrait)
        private set

    val options = Orientation.entries

    fun onOrientationChange(newOrientation: Orientation) {
        orientation = newOrientation
    }

    fun saveSettings() {
        saveSettingsScope.launch {
            listOf(
                async { settingsDao.setOrientation(orientation) },
            ).awaitAll()
            onSavedEventMutable.emit(Unit)
        }
    }
}