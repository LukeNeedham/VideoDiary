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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SetupSelectOrientationViewModel(
    private val settingsDao: SettingsDao,
    ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val saveSettingsScope = CoroutineScope(ioDispatcher)

    private val onSavedEventChannel = Channel<Unit>(Channel.BUFFERED)
    val onSavedEventFlow = onSavedEventChannel.receiveAsFlow()

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
            onSavedEventChannel.send(Unit)
        }
    }
}