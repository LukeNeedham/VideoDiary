package com.lukeneedham.videodiary.ui.feature.setup

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
import java.time.LocalDate

class SetupViewModel(
    private val settingsDao: SettingsDao,
    ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val finaliseSetupScope = CoroutineScope(ioDispatcher)

    private val onCompleteEventMutable = MutableSharedFlow<Unit>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    val onCompleteEventFlow = onCompleteEventMutable.asSharedFlow()

    fun finaliseSetup(resolution: Size, rotation: CameraResolutionRotation) {
        finaliseSetupScope.launch {
            listOf(
                async { settingsDao.setResolution(resolution) },
                async { settingsDao.setResolutionRotation(rotation) },
                async { settingsDao.setStartDate(LocalDate.now()) },
            ).awaitAll()
            onCompleteEventMutable.emit(Unit)
        }
    }
}