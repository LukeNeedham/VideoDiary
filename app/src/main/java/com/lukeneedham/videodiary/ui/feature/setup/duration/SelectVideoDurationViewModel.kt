package com.lukeneedham.videodiary.ui.feature.setup.duration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lukeneedham.videodiary.data.persistence.SettingsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SelectVideoDurationViewModel(
    private val settingsDao: SettingsDao,
    ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val saveSettingsScope = CoroutineScope(ioDispatcher)

    private val onSavedEventMutable = MutableSharedFlow<Unit>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onSavedEventFlow = onSavedEventMutable.asSharedFlow()

    var durationSeconds by mutableIntStateOf(3)
        private set

    fun setDuration(seconds: Int) {
        if (seconds < 1) {
            return
        }
        durationSeconds = seconds
    }

    fun saveSettings() {
        saveSettingsScope.launch {
            listOf(
                async { settingsDao.setVideoDuration(durationSeconds.seconds) },
            ).awaitAll()
            onSavedEventMutable.emit(Unit)
        }
    }
}